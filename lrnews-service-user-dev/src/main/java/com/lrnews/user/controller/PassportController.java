package com.lrnews.user.controller;

import com.lrnews.api.controller.BaseController;
import com.lrnews.api.controller.user.PassportControllerApi;
import com.lrnews.bo.UserInfoBO;
import com.lrnews.enums.UserStatus;
import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.lrnews.pojo.AppUser;
import com.lrnews.user.service.UserService;
import com.lrnews.utils.IPUtil;
import com.lrnews.utils.SMSUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import static com.lrnews.values.CommonApiDefStrings.COOKIE_USER_ID;
import static com.lrnews.values.CommonApiDefStrings.COOKIE_USER_TOKEN;
import static com.lrnews.values.CommonValueInteger.MINIMUM_REQUEST_TIME_SPAN_SECONDS;
import static com.lrnews.values.CommonValueInteger.VERIFY_CODE_TIMEOUT_SECONDS;
import static com.lrnews.values.CommonValueStrings.*;

@RestController
public class PassportController extends BaseController implements PassportControllerApi {

    final static Logger logger = LoggerFactory.getLogger(PassportController.class);

    final SMSUtils sms;

    final UserService userService;

    public PassportController(SMSUtils sms, UserService userService) {
        this.sms = sms;
        this.userService = userService;
    }

    @Override
    public JsonResultObject getVerifyCode(@NotNull @RequestParam String phoneNumber, HttpServletRequest request) {
        String IPLimitTag = REDIS_REQUEST_LIMIT_IP + ":" + IPUtil.getRequestIp(request);
        String randomCode = String.valueOf(new Random().nextInt(899999) + 100000);
        redis.set(IPLimitTag, randomCode, MINIMUM_REQUEST_TIME_SPAN_SECONDS);
        sms.sendVerifyCode(randomCode);
        redis.set(REDIS_MOBILE_VERIFY_CODE + ":" + phoneNumber, randomCode, VERIFY_CODE_TIMEOUT_SECONDS);
        return JsonResultObject.ok(randomCode);
    }

    @Override
    public JsonResultObject doLogin(UserInfoBO userInfo, @NotNull BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            Map<String, String> errors = getErrors(result);
            return JsonResultObject.errorMap(errors);
        }

        String phone = userInfo.getPhone();
        String requestVerifyParam = REDIS_MOBILE_VERIFY_CODE + ':' + phone;
        String code = userInfo.getVerifyCode();

        if (redis.keyExist(requestVerifyParam)) {
            String cachedCode = redis.get(requestVerifyParam);
            if (StringUtils.isBlank(cachedCode) || !code.equals(cachedCode)) {
                logBlocked(phone, ResponseStatusEnum.VERIFY_CODE_INCORRECT.msg());
                return JsonResultObject.errorCustom(ResponseStatusEnum.VERIFY_CODE_INCORRECT);
            }
        } else {
            logBlocked(phone, ResponseStatusEnum.VERIFY_CODE_EXPIRED.msg());
            return JsonResultObject.errorCustom(ResponseStatusEnum.VERIFY_CODE_EXPIRED);
        }

        redis.delete(requestVerifyParam);
        redis.delete(REDIS_REQUEST_LIMIT_IP + ":" + IPUtil.getRequestIp(request));

        AppUser user = userService.queryUserExist(phone);
        if (user != null) {
            if (Objects.equals(user.getActiveStatus(), UserStatus.FROZEN.type)) {
                logBlocked(user.getId(), ResponseStatusEnum.USER_FROZEN.msg());
                return JsonResultObject.errorCustom(ResponseStatusEnum.USER_FROZEN);
            } else {
                logSuccess(user.getMobile(), ResponseStatusEnum.SUCCESS.msg());
                logger.info("Login for user: " + user.getMobile() + " success");
                String token = UUID.randomUUID().toString();
                redis.set(REDIS_USER_TOKEN_KEY + ':' + user.getId(), token);

                setCookie(response, COOKIE_USER_TOKEN, token, DEFAULT_COOKIE_MAX_AGE, false);
                setCookie(response, COOKIE_USER_ID, user.getId(), DEFAULT_COOKIE_MAX_AGE, true);
                return JsonResultObject.ok();
            }
        } else {
            user = userService.createUser(phone);
            if (user != null) {
                logSuccess(user.getMobile(), ResponseStatusEnum.REGISTER_SUCCESS.msg());
                return JsonResultObject.ok(user);
            } else {
                logger.error("Login for new phone number: Create user failed");
                return JsonResultObject.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
            }
        }
    }

    @Override
    public JsonResultObject doLogout(String userId, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        redis.delete(REDIS_USER_TOKEN_KEY + ':' + userId);
        setCookie(response, COOKIE_USER_TOKEN, "", COOKIE_DELETE_AGE, false);
        setCookie(response, COOKIE_USER_ID, "", COOKIE_DELETE_AGE, false);

        return JsonResultObject.ok();
    }

    private static void logBlocked(String idOrPhone, String reason) {
        logger.info("Block login request for user {}: {}", idOrPhone, reason);
    }

    private static void logSuccess(String idOrPhone, String info) {
        logger.info("Login for user {} success: {}", idOrPhone, info);
    }
}

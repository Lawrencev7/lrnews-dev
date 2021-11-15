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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static com.lrnews.values.CommonValueInteger.*;
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
        String IPLimitTag = REQUEST_LIMIT_IP + ":" + IPUtil.getRequestIp(request);
        String randomCode = String.valueOf(new Random().nextInt(899999) + 100000);
        redis.set(IPLimitTag, randomCode, MINIMUM_REQUEST_TIME_SPAN_SECONDS);
        sms.sendVerifyCode(randomCode);
        redis.set(MOBILE_VERIFY_CODE + ":" + phoneNumber, randomCode, VERIFY_CODE_TIMEOUT_SECONDS);
        return JsonResultObject.ok(randomCode);
    }

    @Override
    public JsonResultObject doLogin(UserInfoBO userInfo, @NotNull BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = getErrors(result);
            return JsonResultObject.errorMap(errors);
        }

        String phone = userInfo.getPhone();
        String requestVerifyParam = MOBILE_VERIFY_CODE + ':' + phone;
        String code = userInfo.getVerifyCode();

        if (redis.keyExist(requestVerifyParam)) {
            String cachedCode = redis.get(requestVerifyParam);
            if (StringUtils.isBlank(cachedCode) || !code.equals(cachedCode)) {
                //logger
                return JsonResultObject.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
            }
        }

        AppUser user = userService.queryUserExist(phone);
        if(user != null){
            if(Objects.equals(user.getActiveStatus(), UserStatus.FROZEN.type)) {
                //logger
                return JsonResultObject.errorCustom(ResponseStatusEnum.USER_FROZEN);
            }
        }else {
            user = userService.createUser(phone);
             if(user != null){
                 //logger
                 return JsonResultObject.ok(user);
             }else {
                 //logger
                 return JsonResultObject.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
             }
        }

        return JsonResultObject.ok("?");
    }

    /**
     * Unboxing errors in result which is generated while verifying userInfo
     *
     * @param result BindingResult from http request
     */
    @NotNull
    private Map<String, String> getErrors(@NotNull BindingResult result) {
        Map<String, String> errorMap = new HashMap<>();
        result.getFieldErrors().forEach(e -> {
            errorMap.put(e.getField(), e.getDefaultMessage());
        });
        return errorMap;
    }
}

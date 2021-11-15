package com.lrnews.user.controller;

import com.lrnews.api.controller.BaseController;
import com.lrnews.api.controller.user.PassportControllerApi;
import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.lrnews.utils.IPUtil;
import com.lrnews.utils.SMSUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

@RestController
public class PassportController extends BaseController implements PassportControllerApi {

    final static Logger logger = LoggerFactory.getLogger(PassportController.class);

    final SMSUtils sms;

    public PassportController(SMSUtils sms) {
        this.sms = sms;
    }


    @Override
    public JsonResultObject getVerifyCode(@NotNull HttpServletRequest request) {
        String phoneNumber = request.getParameter("phone");
        if (phoneNumber == null){
            return JsonResultObject.errorMsg(ResponseStatusEnum.FAILED.msg());
        }
        String IPLimitTag = REQUEST_LIMIT_IP + ":" + IPUtil.getRequestIp(request);
//        if(redis.keyExist(IPLimitTag)){
//            Long timeout = redis.getExpire(IPLimitTag).orElse(60L);
//            return JsonResultObject.errorMsg("Apply too frequent, try after " + timeout + " seconds");
//        }

        String randomCode = String.valueOf(new Random().nextInt(899999) + 100000);
        redis.set(IPLimitTag, randomCode, MINIMUM_REQUEST_TIME_SPAN_SECONDS);
        sms.sendVerifyCode(randomCode);
        redis.set(MOBILE_VERIFY_CODE + ":" + phoneNumber, randomCode, VERIFY_CODE_TIMEOUT_SECONDS);
        return JsonResultObject.ok(randomCode);
    }
}

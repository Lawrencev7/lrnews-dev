package com.lrnews.user.controller;

import com.lrnews.api.controller.user.PassportControllerApi;
import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.utils.SMSUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PassportController implements PassportControllerApi {

    final static Logger logger = LoggerFactory.getLogger(PassportController.class);

    final SMSUtils sms;

    public PassportController(SMSUtils sms) {
        this.sms = sms;
    }


    @Override
    public JsonResultObject getVerifyCode() {
        return null;
    }
}

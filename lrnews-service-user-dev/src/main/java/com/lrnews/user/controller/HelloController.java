package com.lrnews.user.controller;

import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.api.controller.user.HelloControllerApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController implements HelloControllerApi {
    final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    public Object hello(){
        return JsonResultObject.error();
    }
}

package com.lrnews.user.controller;

import com.lrnews.api.controller.HelloControllerApi;
import com.lrnews.graceresult.JsonResultObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserHelloController implements HelloControllerApi {
    @Value("${server.port}")
    private String port;

    @Bean
    String getPort(){
        return port;
    }

    final static Logger logger = LoggerFactory.getLogger(UserHelloController.class);

    public Object hello() {
        return JsonResultObject.error();
    }

    @GetMapping("/test")
    public String test(){
        System.out.println("This interface is called by port: " + port);
        return "port->" + port;
    }
}

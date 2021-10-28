package com.lrnews.user.controller;

import com.lrnews.api.controller.user.HelloControllerApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController implements HelloControllerApi {

    public Object hello(){
        return "hello";
    }
}

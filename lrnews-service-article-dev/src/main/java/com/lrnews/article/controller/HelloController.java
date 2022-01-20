package com.lrnews.article.controller;

import com.lrnews.api.controller.HelloControllerApi;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController implements HelloControllerApi {

    @Override
    public Object hello() {
        return "Hello Article Controller";
    }
}

package com.lrnews.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(value = "Test controller api value", tags = {"hello-controller"})
@RequestMapping("/hello")
//@FeignClient(value = SERVICE_USER)
public interface HelloControllerApi {

    @GetMapping("/hello")
    @ApiOperation(value = "test function: hello", notes = "hello function in hello-controller ")
    Object hello();

    @GetMapping("/helloRabbitMQ")
    @ApiOperation(value = "test for rabbit mq", notes = "rabbit mq test function")
    default Object helloRabbitMQ(){
        return null;
    }

    @GetMapping("/test")
    default Object test() {return "";}
}

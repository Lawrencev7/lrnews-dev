package com.lrnews.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

@Api(value = "Test controller api value", tags = {"hello-controller", "The first test controller for this project"})
public interface HelloControllerApi {

    @GetMapping("/hello")
    @ApiOperation(value = "test function: hello", notes = "hello function in hello-controller ")
    Object hello();
}

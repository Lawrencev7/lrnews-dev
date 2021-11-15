package com.lrnews.api.controller.user;

import com.lrnews.graceresult.JsonResultObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Api(value = "Passport related server controller", tags = {"Passport server controller"})
@RequestMapping("/verify")
public interface PassportControllerApi {

    @GetMapping("/get-verify-code")
    @ApiOperation(value = "Get register or login message code", notes = "Get SMS verify code")
    JsonResultObject getVerifyCode(HttpServletRequest request);
}

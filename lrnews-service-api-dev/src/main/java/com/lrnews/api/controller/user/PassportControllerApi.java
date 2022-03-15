package com.lrnews.api.controller.user;

import com.lrnews.bo.UserInfoBO;
import com.lrnews.graceresult.JsonResultObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api(value = "Passport related server controller", tags = {"Passport server controller"})
@RequestMapping("/verify")
public interface PassportControllerApi {

    @GetMapping("/get-verify-code")
    @ApiOperation(value = "Get register or login message code", notes = "Get SMS verify code")
    JsonResultObject getVerifyCode(@RequestParam String phoneNumber, HttpServletRequest request);

    @PostMapping("/login")
    @ApiOperation(value = "Login/register interface", notes = "Do login or register in this interface")
    JsonResultObject doLogin(@Valid @RequestBody UserInfoBO userInfo, BindingResult result, HttpServletRequest request, HttpServletResponse response);

    @PostMapping("/logout")
    @ApiOperation(value = "Logout interface", notes = "Delete cached user information with this interface")
    JsonResultObject doLogout(String userId, BindingResult result, HttpServletRequest request, HttpServletResponse response);
}

package com.lrnews.api.controller.user;

import com.lrnews.bo.UpdateUserInfoBO;
import com.lrnews.graceresult.JsonResultObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Api(value = "User info api", tags = {"An entry for user infos"})
@RequestMapping("/user")
public interface UserInfoControllerApi {

    @ApiOperation(value = "Interface for obtaining user full info", tags = {"Get user info"})
    @PostMapping("/get-user-info")
    JsonResultObject getUserInfo(@RequestParam String userId);

    @ApiOperation(value = "Interface for obtaining user common info", tags = {"Get user common info"})
    @PostMapping("/get-common-info")
    JsonResultObject getUserCommonInfo(@RequestParam String userId);

    @ApiOperation(value = "Interface for updating user info", tags = {"Update user info"})
    @PostMapping("/update-user-info")
    JsonResultObject updateUserInfo(@RequestBody @Valid UpdateUserInfoBO updateUserInfoBO, BindingResult result);

}

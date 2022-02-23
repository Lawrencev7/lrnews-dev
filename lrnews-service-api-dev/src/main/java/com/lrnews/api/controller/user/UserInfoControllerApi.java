package com.lrnews.api.controller.user;

import com.lrnews.bo.UpdateUserInfoBO;
import com.lrnews.graceresult.JsonResultObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "User info api", tags = {"An entry for user infos"})
@RequestMapping("/user")
public interface UserInfoControllerApi {

    @ApiOperation(value = "Interface for obtaining user full info", tags = {"Get user info"})
    @PostMapping("/getUserInfo")
    JsonResultObject getUserInfo(@RequestParam String userId);

    @ApiOperation(value = "Interface for obtaining user common info", tags = {"Get user common info"})
    @PostMapping("/getCommonInfo")
    JsonResultObject getUserCommonInfo(@RequestParam String userId);

    @ApiOperation(value = "Interface for updating user info", tags = {"Update user info"})
    @PostMapping("/updateUserInfo")
    JsonResultObject updateUserInfo(@RequestBody @Valid UpdateUserInfoBO updateUserInfoBO, BindingResult result);

    @ApiOperation(value = "Query user info by user ids string", tags = {"Query by ids string"})
    @GetMapping("/queryUserByIds")
    JsonResultObject queryUserByIds(@RequestParam String userIds);

}

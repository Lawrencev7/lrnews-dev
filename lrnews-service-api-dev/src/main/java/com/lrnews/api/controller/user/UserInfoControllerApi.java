package com.lrnews.api.controller.user;

import com.lrnews.api.controller.user.fallbacks.UserInfoCtrlFallback;
import com.lrnews.bo.UpdateUserInfoBO;
import com.lrnews.graceresult.JsonResultObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.lrnews.api.values.ServiceList.SERVICE_USER;

@Api(value = "User info api")
//@RequestMapping("/user")
@FeignClient(value = SERVICE_USER, fallbackFactory = UserInfoCtrlFallback.class)
public interface UserInfoControllerApi {

    @ApiOperation(value = "Interface for obtaining user full info")
    @PostMapping("/user/getUserInfo")
    JsonResultObject getUserInfo(@RequestParam String userId);

    @ApiOperation(value = "Interface for obtaining user common info")
    @PostMapping("/user/getCommonInfo")
    JsonResultObject getUserCommonInfo(@RequestParam String userId);

    @ApiOperation(value = "Interface for updating user info")
    @PostMapping("/user/updateUserInfo")
    JsonResultObject updateUserInfo(@RequestBody @Valid UpdateUserInfoBO updateUserInfoBO);

    @ApiOperation(value = "Query user info by user ids string")
    @GetMapping("/user/queryUserByIds")
    JsonResultObject queryUserByIds(@RequestParam String userIds);

    @ApiOperation(value = "Query personal page info")
    @GetMapping("/user/getPersonalPageInfo")
    JsonResultObject getPersonalPageInfo(@RequestParam String userIds,
                                         @ApiParam(name = "page", value = "Current query page")
                                         @RequestParam Integer page,
                                         @ApiParam(name = "pageSize", value = "Current page size")
                                         @RequestParam Integer pageSize,
                                         @RequestParam HttpServletRequest request);

    @ApiOperation(value = "Simulate a block event")
    @GetMapping("/user/simBlock")
    JsonResultObject blockSimulator();

}

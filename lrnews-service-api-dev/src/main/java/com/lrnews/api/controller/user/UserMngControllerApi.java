package com.lrnews.api.controller.user;

import com.lrnews.graceresult.JsonResultObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Api(value = "User manage controller", tags = {"User manage"})
@RequestMapping("/userMng")
public interface UserMngControllerApi {

    @ApiOperation(value = "Query all user", notes = "Query all app users")
    @PostMapping("/queryAll")
    JsonResultObject queryAllUser(@RequestParam String nickname,
                                  @RequestParam Integer status,
                                  @RequestParam Date startDate,
                                  @RequestParam Date endDate,
                                  @RequestParam Integer page,
                                  @RequestParam Integer pageSize);

    @ApiOperation(value = "Get user details", notes = "Get user details by user id")
    @PostMapping("/userDetails")
    JsonResultObject userDetails(@RequestParam String userId);

    @ApiOperation(value = "Set user status", notes = "0=inactive, 1=active, 2=FROZEN.")
    @PostMapping("/setStatus")
    JsonResultObject setStatus(@RequestParam String userId, @RequestParam String doStatus);
}

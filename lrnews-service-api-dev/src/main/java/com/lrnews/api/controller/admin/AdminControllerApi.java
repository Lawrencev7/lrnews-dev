package com.lrnews.api.controller.admin;

import com.lrnews.bo.AdminLoginBO;
import com.lrnews.bo.NewAdminBO;
import com.lrnews.graceresult.JsonResultObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "Admin management controller", tags = {"Admin Service", "Controller"})
@RequestMapping("/admin")
public interface AdminControllerApi {

    @ApiOperation(value = "Admin user login", notes = "Login for a admin user with username and password")
    @PostMapping("/adminLogin")
    JsonResultObject adminLogin(@RequestBody AdminLoginBO adminLoginBO,
                                HttpServletRequest request, HttpServletResponse response);

    @ApiOperation(value = "Admin user query", notes = "Check if the username is existed")
    @PostMapping("/adminIsExist")
    JsonResultObject adminIsExist(@RequestParam String username,
                                  HttpServletRequest request, HttpServletResponse response);

    @ApiOperation(value = "Create new admin", notes = "Create new Admin user")
    @PostMapping("/createNewAdmin")
    JsonResultObject createNewAdmin(@RequestBody NewAdminBO newAdmin,
                                    HttpServletRequest request, HttpServletResponse response);

    @ApiOperation(value = "Query admin list", notes = "Get a list contains all admins")
    @PostMapping("/getAdminList")
    JsonResultObject queryAdminList(@ApiParam(value = "Indicates that which page is requested", name = "page") @RequestParam
                                            Integer page,
                                    @ApiParam(value = "Indicates the capacity of one page", name = "pageSize") @RequestParam
                                            Integer pageSize);
}

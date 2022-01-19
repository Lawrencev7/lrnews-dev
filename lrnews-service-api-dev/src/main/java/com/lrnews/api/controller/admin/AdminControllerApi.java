package com.lrnews.api.controller.admin;

import com.lrnews.bo.AdminLoginBO;
import com.lrnews.bo.AdminBO;
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

@Api(value = "Admin management controller", tags = {"Admin", "Controller"})
@RequestMapping("/admin")
public interface AdminControllerApi {

    @ApiOperation(value = "Admin user login", notes = "Login for a admin user with username and password")
    @PostMapping("/adminLogin")
    JsonResultObject adminLogin(@RequestBody AdminLoginBO adminLoginBO,
                                HttpServletRequest request, HttpServletResponse response);

    @ApiOperation(value = "Admin user exist", notes = "Check if the username is existed")
    @PostMapping("/adminIsExist")
    JsonResultObject adminIsExist(@RequestParam String username,
                                  HttpServletRequest request, HttpServletResponse response);

    @ApiOperation(value = "Create new admin", notes = "Create new Admin user")
    @PostMapping("/createNewAdmin")
    JsonResultObject createNewAdmin(@RequestBody AdminBO newAdmin,
                                    HttpServletRequest request, HttpServletResponse response);

    @ApiOperation(value = "Query admin list", notes = "Get a list contains all admins")
    @PostMapping("/getAdminList")
    JsonResultObject getAdminList(@ApiParam(value = "Indicates that which page is requested", name = "page") @RequestParam
                                            Integer page,
                                  @ApiParam(value = "Indicates the capacity of one page", name = "pageSize") @RequestParam
                                            Integer pageSize);

    @ApiOperation(value = "Logout for admin", notes = "Logout for admin")
    @PostMapping("/adminLogout")
    JsonResultObject adminLogout(@RequestParam String adminId,
                                    HttpServletRequest request, HttpServletResponse response);

    @ApiOperation(value = "Admin face login", notes = "Login for admin by face recognition")
    @PostMapping("/faceRecLogin")
    JsonResultObject faceRecLogin(@RequestBody AdminLoginBO adminLoginBO,
                                 HttpServletRequest request, HttpServletResponse response);

    JsonResultObject uploadFaceImg64();
}

package com.lrnews.admin.controller;

import com.lrnews.admin.service.AdminUserService;
import com.lrnews.api.controller.BaseController;
import com.lrnews.api.controller.admin.AdminControllerApi;
import com.lrnews.bo.AdminLoginBO;
import com.lrnews.bo.NewAdminBO;
import com.lrnews.exception.CustomExceptionFactory;
import com.lrnews.exception.LrCustomException;
import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.lrnews.pojo.AdminUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.UUID;

import static com.lrnews.values.CommonValueStrings.REDIS_ADMIN_TOKEN_KEY;

@RestController
public class AdminController extends BaseController implements AdminControllerApi{
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    final AdminUserService adminUserService;

    public AdminController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @Override
    public JsonResultObject adminLogin(AdminLoginBO adminLoginBO,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        if(StringUtils.isBlank(adminLoginBO.getUsername())){
            logBlocked("UNKNOWN", ResponseStatusEnum.ADMIN_USERNAME_NULL_ERROR.msg());
            return JsonResultObject.errorCustom(ResponseStatusEnum.ADMIN_USERNAME_NULL_ERROR);
        }else if (StringUtils.isBlank(adminLoginBO.getPassword())){
            logBlocked(adminLoginBO.getUsername(), ResponseStatusEnum.ADMIN_PASSWORD_NULL_ERROR.msg());
            return JsonResultObject.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_NULL_ERROR);
        }

        AdminUser adminUser = adminUserService.queryAdminUserByUsername(adminLoginBO.getUsername());
        if(adminUser == null){
            logBlocked("NOT EXIST", ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR.msg());
            return JsonResultObject.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }

        boolean pwdCheck = BCrypt.checkpw(adminLoginBO.getPassword(), adminUser.getPassword());
        if(pwdCheck){
            logSuccess(adminUser.getId());
            loginInfoCache(adminUser, request, response);
            return JsonResultObject.ok();
        }else {
            logBlocked(adminUser.getId(), ResponseStatusEnum.ADMIN_PWD_WRONG_ERROR.msg());
            return JsonResultObject.errorCustom(ResponseStatusEnum.ADMIN_PWD_WRONG_ERROR);
        }
    }

    @Override
    public JsonResultObject adminIsExist(String username, HttpServletRequest request, HttpServletResponse response) {
        checkAdminExist(username);
        return JsonResultObject.ok();
    }

    @Override
    public JsonResultObject createNewAdmin(@RequestBody NewAdminBO admin,
                                           HttpServletRequest request, HttpServletResponse response) {

        if(StringUtils.isBlank(admin.getImg64())){
            if(StringUtils.isBlank(admin.getPassword()) || StringUtils.isNotBlank(admin.getConfirmPassword())){
                return JsonResultObject.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_NULL_ERROR);
            }

            if(!admin.getPassword().equals(admin.getConfirmPassword())){
                return JsonResultObject.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
            }
        }

        checkAdminExist(admin.getUsername());

        return null;
    }

    private void checkAdminExist(String username){
        AdminUser adminUser = adminUserService.queryAdminUserByUsername(username);
        if(adminUser != null ){
            CustomExceptionFactory.onException(ResponseStatusEnum.ADMIN_USERNAME_EXIST_ERROR);
        }
    }

    private void loginInfoCache(AdminUser adminUser,
                                HttpServletRequest request, HttpServletResponse response){
        String token = UUID.randomUUID().toString();

        // save user token to redis server
        redis.set(REDIS_ADMIN_TOKEN_KEY + ':' + adminUser.getId(), token);

        // save user token in cookie
        setCookie(response, COOKIE_ADMIN_TOKEN, token, DEFAULT_COOKIE_MAX_AGE, false);
        setCookie(response, COOKIE_ADMIN_ID, adminUser.getId(), DEFAULT_COOKIE_MAX_AGE, false);
        setCookie(response, COOKIE_ADMIN_NAME, adminUser.getAdminName(), DEFAULT_COOKIE_MAX_AGE, false);
    }

    private static void logSuccess(String id) {
        logger.info("Login for admin {} success", id);
    }

    private static void logBlocked(String id, String reason) {
        logger.info("Block login request for admin {}: {}", id, reason);
    }
}

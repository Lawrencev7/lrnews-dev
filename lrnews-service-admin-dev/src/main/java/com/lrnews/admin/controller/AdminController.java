package com.lrnews.admin.controller;

import com.github.pagehelper.PageInfo;
import com.lrnews.admin.service.AdminUserService;
import com.lrnews.api.controller.BaseController;
import com.lrnews.api.controller.admin.AdminControllerApi;
import com.lrnews.bo.AdminLoginBO;
import com.lrnews.bo.NewAdminBO;
import com.lrnews.exception.CustomExceptionFactory;
import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.lrnews.pojo.AdminUser;
import com.lrnews.utils.FaceCognitionUtil;
import com.lrnews.vo.PagedGridVO;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.Integers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.lrnews.values.CommonApiDefStrings.*;
import static com.lrnews.values.CommonValueStrings.REDIS_ADMIN_TOKEN_KEY;

@RestController
public class AdminController extends BaseController implements AdminControllerApi {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    public static final int DEFAULT_PAGE = 1;
    public static final int DEFAULT_PAGE_SIZE = 10;

    final AdminUserService adminUserService;

    final RestTemplate restTemplate;

    public AdminController(AdminUserService adminUserService, RestTemplate restTemplate) {
        this.adminUserService = adminUserService;
        this.restTemplate = restTemplate;
    }

    @Override
    public JsonResultObject adminLogin(AdminLoginBO adminLoginBO,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        if (StringUtils.isBlank(adminLoginBO.getUsername())) {
            logBlocked(ResponseStatusEnum.ADMIN_USERNAME_NULL_ERROR.msg());
            return JsonResultObject.errorCustom(ResponseStatusEnum.ADMIN_USERNAME_NULL_ERROR);
        } else if (StringUtils.isBlank(adminLoginBO.getPassword())) {
            logBlocked(adminLoginBO.getUsername(), ResponseStatusEnum.ADMIN_PASSWORD_NULL_ERROR.msg());
            return JsonResultObject.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_NULL_ERROR);
        }

        AdminUser adminUser = adminUserService.queryAdminUserByUsername(adminLoginBO.getUsername());
        if (adminUser == null) {
            logBlocked(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR.msg());
            return JsonResultObject.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }

        boolean pwdCheck = BCrypt.checkpw(adminLoginBO.getPassword(), adminUser.getPassword());
        if (pwdCheck) {
            logSuccess(adminUser.getId());
            loginInfoCache(adminUser, request, response);
            return JsonResultObject.ok();
        } else {
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

        if (StringUtils.isBlank(admin.getImg64())) {
            if (StringUtils.isBlank(admin.getPassword()) || StringUtils.isNotBlank(admin.getConfirmPassword())) {
                return JsonResultObject.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_NULL_ERROR);
            }

            if (!admin.getPassword().equals(admin.getConfirmPassword())) {
                return JsonResultObject.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
            }
        }

        checkAdminExist(admin.getUsername());

        adminUserService.createAdminUser(admin);
        return JsonResultObject.ok();
    }

    @Override
    public JsonResultObject getAdminList(Integer page, Integer pageSize) {
        if (Objects.isNull(page)) {
            page = Integers.valueOf(DEFAULT_PAGE);
        }

        if (Objects.isNull(pageSize)) {
            pageSize = Integers.valueOf(DEFAULT_PAGE_SIZE);
        }

        List<AdminUser> adminUsers = adminUserService.queryAdminListPageable(page, pageSize);
        if (adminUsers.size() != 0) {
            return JsonResultObject.ok(setPagedGrid(adminUsers, page));
        } else
            return JsonResultObject.ok("No more infos");
    }

    @Override
    public JsonResultObject adminLogout(String adminId, HttpServletRequest request, HttpServletResponse response) {
        // Remove admin info from redis
        redis.delete(REDIS_ADMIN_TOKEN_KEY + ':' + adminId);

        // Remove admin info from cookie
        deleteCookies(response, COOKIE_ADMIN_TOKEN, COOKIE_ADMIN_ID, COOKIE_ADMIN_NAME);

        return JsonResultObject.ok();
    }

    @Override
    public JsonResultObject uploadFaceImg64() {
        return null;
    }

    @Override
    public JsonResultObject faceRecLogin(AdminLoginBO adminLoginBO, HttpServletRequest request, HttpServletResponse response) {
        String adminUsername = adminLoginBO.getUsername();
        if (StringUtils.isBlank(adminUsername)) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.ADMIN_USERNAME_NULL_ERROR);
        }

        String faceImg64 = adminLoginBO.getImg64();
        if (StringUtils.isBlank(faceImg64)) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.ADMIN_FACE_NULL_ERROR);
        }

        String faceId = adminUserService.queryAdminUserByUsername(adminUsername).getFaceId();
        if (StringUtils.isBlank(faceId)) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.ADMIN_FACE_NOT_REGISTERED_ERROR);
        }

        // Request for file server and get face img
        String fileServerUrl = "http://localhost:8004/file/readFaceImg64?faceId=" + faceId;
        String img64String = restTemplate.getForObject(fileServerUrl, String.class);

        // Request for face cognition server <Ignored here>
        boolean result = FaceCognitionUtil.verifyFace(faceImg64);
        if (result) {
            AdminUser user = adminUserService.queryAdminUserByUsername(adminUsername);
            loginInfoCache(user, request, response);
            logSuccess(user.getId());
            return JsonResultObject.ok();
        } else {
            logBlocked(ResponseStatusEnum.ADMIN_FACE_LOGIN_ERROR.msg());
            return JsonResultObject.errorCustom(ResponseStatusEnum.ADMIN_FACE_LOGIN_ERROR);
        }
    }

    /**
     * Private functions
     */

    private void checkAdminExist(String username) {
        AdminUser adminUser = adminUserService.queryAdminUserByUsername(username);
        if (adminUser != null) {
            CustomExceptionFactory.onException(ResponseStatusEnum.ADMIN_USERNAME_EXIST_ERROR);
        }
    }

    private void loginInfoCache(AdminUser adminUser,
                                HttpServletRequest request, HttpServletResponse response) {
        String token = UUID.randomUUID().toString();

        // save user token to redis server
        redis.set(REDIS_ADMIN_TOKEN_KEY + ':' + adminUser.getId(), token);

        // save user token in cookie
        setCookie(response, COOKIE_ADMIN_TOKEN, token, DEFAULT_COOKIE_MAX_AGE, false);
        setCookie(response, COOKIE_ADMIN_ID, adminUser.getId(), DEFAULT_COOKIE_MAX_AGE, false);
        setCookie(response, COOKIE_ADMIN_NAME, adminUser.getAdminName(), DEFAULT_COOKIE_MAX_AGE, false);
    }

    private PagedGridVO setPagedGrid(List<?> list, Integer page) {
        PageInfo<?> pageInfo = new PageInfo<>(list);

        PagedGridVO pagedGridVO = new PagedGridVO();
        pagedGridVO.setRows(list);
        pagedGridVO.setPage(page);
        pagedGridVO.setRecords(pageInfo.getPages());
        pagedGridVO.setTotal(pageInfo.getTotal());

        return pagedGridVO;
    }

    private static void logSuccess(String id) {
        logger.info("Login for admin {} success", id);
    }

    private static void logBlocked(String id, String reason) {
        logger.info("Block login request for admin {}: {}", id, reason);
    }

    private static void logBlocked(String reason) {
        logBlocked("-", reason);
    }
}

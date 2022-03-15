package com.lrnews.api.controller;

import com.lrnews.exception.CustomExceptionFactory;
import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.lrnews.utils.JsonUtils;
import com.lrnews.utils.RedisOperator;
import com.lrnews.vo.CommonUserVO;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.lrnews.values.CommonValueStrings.REDIS_CATEGORY_KEY;


public class BaseController {

    protected static final Integer DEFAULT_KEY_MAX_AGE = 7 * 24 * 60 * 60; // Cookie stays one week
    protected static final Integer ONE_DAY_KEY_AGE = 24 * 60 * 60; // Cookie stays one week
    protected static final Integer COOKIE_DELETE_AGE = 0; // For deleted cookie

    protected static final String REMOTE_CALL_QUERY_USER_BY_IDS_URL = "http://localhost:8003/user/queryUserByIds?userIds=";

    protected static String DEFAULT_COOKIE_DOMAIN = "lrnews.com";

    public static final int DEFAULT_PAGE = 1;
    public static final int DEFAULT_PAGE_SIZE = 10;

    @Autowired
    protected RedisOperator redis;

    @Autowired
    protected RestTemplate restTemplate;

    protected void setCookie(HttpServletResponse response, String cookieKey,
                             String cookieValue, Integer maxAge, boolean needEncode) {
        if (needEncode) {
            cookieValue = URLEncoder.encode(cookieValue, StandardCharsets.UTF_8);
        }

        Cookie cookie = new Cookie(cookieKey, cookieValue);
        cookie.setMaxAge(maxAge);
        cookie.setDomain(DEFAULT_COOKIE_DOMAIN);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    protected void deleteCookies(HttpServletResponse response, String... keys) {
        for (String key : keys) {
            setCookie(response, key, "", COOKIE_DELETE_AGE, false);
        }
    }

    /**
     * Unboxing errors in result which is generated while verifying userInfo
     *
     * @param result BindingResult from http request
     */
    @NotNull
    protected Map<String, String> getErrors(@NotNull BindingResult result) {
        Map<String, String> errorMap = new HashMap<>();
        result.getFieldErrors().forEach(e -> errorMap.put(e.getField(), e.getDefaultMessage()));
        return errorMap;
    }

    protected void deleteRedisCategoryCache() {
        redis.delete(REDIS_CATEGORY_KEY);
    }

    protected List<CommonUserVO> remoteQueryUserInfos(Set<String> userIds) {
        List<CommonUserVO> publisherList = null;
        String userServerUrl = REMOTE_CALL_QUERY_USER_BY_IDS_URL + JsonUtils.objectToJson(userIds);
        ResponseEntity<JsonResultObject> entity = restTemplate.getForEntity(userServerUrl, JsonResultObject.class);
        JsonResultObject responseData = entity.getBody();

        if (!Objects.nonNull(responseData)) {
            CustomExceptionFactory.onException(ResponseStatusEnum.SYSTEM_CONNECTION_FAIL);
        }

        if (responseData.getStatus().equals(ResponseStatusEnum.SUCCESS.status())) {
            String userJson = JsonUtils.objectToJson(responseData.getData());
            publisherList = JsonUtils.jsonToList(userJson, CommonUserVO.class);

            if (publisherList == null) {
                CustomExceptionFactory.onException(ResponseStatusEnum.SYSTEM_ERROR);
            }
        } else {
            CustomExceptionFactory.onException(ResponseStatusEnum.SYSTEM_ERROR);
        }

        return publisherList;
    }

    protected CommonUserVO remoteQueryUserInfo(String userId) {
        return remoteQueryUserInfos(Set.of(userId)).get(0);
    }
}

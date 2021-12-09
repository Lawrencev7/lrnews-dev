package com.lrnews.api.controller;

import com.lrnews.utils.RedisOperator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class BaseController {
    protected static final Integer DEFAULT_COOKIE_MAX_AGE = 7 * 24 * 60 * 60; // Cookie stays one week

    protected static String DEFAULT_COOKIE_DOMAIN = "lrnews.com";

    @Autowired
    protected RedisOperator redis;

    protected void setCookie(HttpServletResponse response, String cookieKey,
                             String cookieValue, Integer maxAge, boolean needEncode){
        if(needEncode){
            cookieValue = URLEncoder.encode(cookieValue, StandardCharsets.UTF_8);
        }

        Cookie cookie = new Cookie(cookieKey, cookieValue);
        cookie.setMaxAge(maxAge);
        cookie.setDomain(DEFAULT_COOKIE_DOMAIN);
        cookie.setPath("/");
        response.addCookie(cookie);
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
}

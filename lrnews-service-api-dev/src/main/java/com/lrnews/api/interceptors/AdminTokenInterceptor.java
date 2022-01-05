package com.lrnews.api.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.lrnews.values.CommonApiDefStrings.*;

@Configuration
public class AdminTokenInterceptor  extends BaseInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uid = request.getHeader(SESSION_HEADER_ADMIN_ID);
        String token = request.getHeader(SESSION_HEADER_USER_TOKEN);
        logger.info("Intercept request for user: " + uid);
        return verifyAdmin(uid,token);
    }
}

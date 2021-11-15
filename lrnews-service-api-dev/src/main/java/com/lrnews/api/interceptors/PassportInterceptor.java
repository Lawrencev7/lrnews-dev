package com.lrnews.api.interceptors;

import com.lrnews.api.controller.BaseController;
import com.lrnews.exception.CustomExceptionFactory;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.lrnews.utils.IPUtil;
import com.lrnews.utils.RedisOperator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.lrnews.values.CommonValueInteger.*;
import static com.lrnews.values.CommonValueStrings.*;

@Configuration
public class PassportInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(PassportInterceptor.class);

    RedisOperator redis;

    public PassportInterceptor(RedisOperator r){
        redis = r;
    }

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) throws Exception {
        String IP = IPUtil.getRequestIp(request);
        if(redis.keyExist(REQUEST_LIMIT_IP + ":" + IP)){
            logger.info("Block user request: User request has been rejected due to over frequent apply");
            CustomExceptionFactory.onException(ResponseStatusEnum.SMS_OVER_FREQUENT_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(@NotNull HttpServletRequest request,
                           @NotNull HttpServletResponse response,
                           @NotNull Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request,
                                @NotNull HttpServletResponse response,
                                @NotNull Object handler, Exception ex) throws Exception {

    }
}

package com.lrnews.api.interceptors;

import com.lrnews.exception.CustomExceptionFactory;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.lrnews.utils.IPUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.lrnews.values.CommonRedisKeySet.REDIS_REQUEST_LIMIT_IP_KEY;

@Configuration
public class PassportInterceptor extends BaseInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(PassportInterceptor.class);

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) throws Exception {
        String IP = IPUtil.getRequestIp(request);
        if (redis.keyExist(REDIS_REQUEST_LIMIT_IP_KEY + IP)) {
            logger.info("Block user request: User request has been rejected due to over frequent apply");
            CustomExceptionFactory.onException(ResponseStatusEnum.SMS_OVER_FREQUENT_ERROR);
            return false;
        }
        return true;
    }
}

package com.lrnews.api.interceptors;

import com.lrnews.enums.UserStatus;
import com.lrnews.exception.CustomExceptionFactory;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.lrnews.pojo.AppUser;
import com.lrnews.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

import static com.lrnews.values.CommonApiDefStrings.SESSION_HEADER_USER_ID;
import static com.lrnews.values.CommonValueStrings.REDIS_USER_CACHE_TAG;

@Configuration
public class UserActiveInterceptor extends BaseInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uid = request.getHeader(SESSION_HEADER_USER_ID);
        String userJson = redis.get(REDIS_USER_CACHE_TAG + ':' + uid);
        logger.info("Intercept request for user: " + uid);
        if(StringUtils.isNotBlank(userJson)){
            AppUser appUser = JsonUtils.jsonToPojo(userJson, AppUser.class);
            if(appUser == null || Objects.equals(appUser.getActiveStatus(), UserStatus.INACTIVE.type)){
                CustomExceptionFactory.onException(ResponseStatusEnum.USER_INACTIVE_ERROR);
                return false;
            }

            return true;
        }else {
            CustomExceptionFactory.onException(ResponseStatusEnum.USER_NOT_LOGIN);
            return false;
        }
    }
}

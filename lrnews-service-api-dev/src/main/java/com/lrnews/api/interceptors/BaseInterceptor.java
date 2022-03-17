package com.lrnews.api.interceptors;

import com.lrnews.exception.CustomExceptionFactory;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.lrnews.utils.RedisOperator;
import com.lrnews.values.CommonRedisKeySet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseInterceptor {
    @Autowired
    RedisOperator redis;

    public boolean verifyUser(String uid, String uToken) {
        if (StringUtils.isBlank(uid) || StringUtils.isBlank(uToken)) {
            CustomExceptionFactory.onException(ResponseStatusEnum.USER_NOT_LOGIN);
            return false;
        }

        String redisToken = redis.get(CommonRedisKeySet.REDIS_USER_TOKEN_KEY + uid);
        if (StringUtils.isBlank(redisToken) || !redisToken.equalsIgnoreCase(uToken)) {
            CustomExceptionFactory.onException(ResponseStatusEnum.TICKET_INVALID);
            return false;
        }

        return true;
    }

    public boolean verifyAdmin(String uid, String uToken) {
        if (StringUtils.isBlank(uid) || StringUtils.isBlank(uToken)) {
            CustomExceptionFactory.onException(ResponseStatusEnum.USER_NOT_LOGIN);
            return false;
        }

        String redisToken = redis.get(CommonRedisKeySet.REDIS_ADMIN_TOKEN_KEY + uid);
        if (StringUtils.isBlank(redisToken) || !redisToken.equalsIgnoreCase(uToken)) {
            CustomExceptionFactory.onException(ResponseStatusEnum.TICKET_INVALID);
            return false;
        }

        return true;
    }
}

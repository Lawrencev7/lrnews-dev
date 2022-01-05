package com.lrnews.values;

public class CommonValueStrings {
    // User SMS request time limit in redis
    public static final String REDIS_REQUEST_LIMIT_IP = "REQUEST:LIMIT:FOR";

    // Cached mobile verify code in redis
    public static final String REDIS_MOBILE_VERIFY_CODE = "VERIFY:CODE";

    // User token key prefix in redis
    public static final String REDIS_USER_TOKEN_KEY = "TOKEN:FOR:USER";

    // Admin user token key prefix in redis
    public static final String REDIS_ADMIN_TOKEN_KEY = "TOKEN:FOR:ADMIN";

    // User common info key prefix in redis
    public static final String REDIS_USER_CACHE_TAG = "CACHED:USER:INFO";

    public static final String DEFAULT_USER_AVATAR = "https://imooc-new/s.oss-cn-shanghai.aliyuncs.com/image/face/200412A6A3DSZ0X4/200412A6A3DSZ0X4.png";
}

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

    // All categories in redis
    public static final String REDIS_CATEGORY_KEY = "ALL:CATEGORY";

    public static final String REDIS_WRITER_FOLLOWER_NUM_KEY = "FOLLOWER:NUM:OF";

    public static final String REDIS_MY_SUBSCRIBE_NUM_KEY = "SUBSCRIBE:NUM:OF";

    public static final String REDIS_ARTICLE_READ_COUNT_KEY = "READ:COUNT";

    // Usage: When one user (identified by session id) read an article multiple times, only counts 1 time per 24 hours.
    //        Use this key to save this relation in redis.
    // Example: Save key=IP + REDIS_IP_READ_LINK_KEY + ArticleID and value=Anything to indicate that this IP has read this article
    public static final String REDIS_IP_READ_LINK_KEY = ":HAS:READ:IN24H:";
}

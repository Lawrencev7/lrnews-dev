package com.lrnews.values;

public class CommonRedisKeySet {
    // User SMS request time limit in redis
    public static final String REDIS_REQUEST_LIMIT_IP_KEY = "REQUEST:LIMIT:FOR:";

    // Cached mobile verify code in redis
    public static final String REDIS_MOBILE_VERIFY_CODE_KEY = "VERIFY:CODE:";

    // User token key prefix in redis
    public static final String REDIS_USER_TOKEN_KEY = "TOKEN:FOR:USER:";

    // Admin user token key prefix in redis
    public static final String REDIS_ADMIN_TOKEN_KEY = "TOKEN:FOR:ADMIN:";

    // User common info key prefix in redis
    public static final String REDIS_USER_CACHE_KEY = "CACHED:USER:INFO:";

    // All categories in redis
    public static final String REDIS_CATEGORY_KEY = "ALL:CATEGORY";

    public static final String REDIS_WRITER_FOLLOWER_NUM_KEY = "FOLLOWER:NUM:OF:";

    public static final String REDIS_MY_SUBSCRIBE_NUM_KEY = "SUBSCRIBE:NUM:OF:";

    public static final String REDIS_ARTICLE_READ_COUNT_KEY = "READ:COUNT:";

    // Usage: When one user (identified by session id) read an article multiple times, only counts 1 time per 24 hours.
    //        Use this key to save this relation in redis.
    // Example: Save key=IP + REDIS_IP_READ_LINK_KEY + ArticleID and value=Anything to indicate that this IP has read this article
    public static final String REDIS_IP_READ_LINK_KEY = ":HAS:READ:IN24H:";

    public static final String REDIS_ARTICLE_COMMENT_COUNT_KEY = "COMMENT:COUNT:";

    public static final String REDIS_ZUUL_IP_REQUEST_TIME_KEY = "ZUUL:IP:REQUEST:TIME:";

    public static final String REDIS_ZUUL_BLOCKED_IP_KEY = "ZUUL:IP:BLOCKED:";
}

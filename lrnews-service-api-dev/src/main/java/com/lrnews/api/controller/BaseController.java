package com.lrnews.api.controller;

import com.lrnews.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;


public class BaseController {
    @Autowired
    protected RedisOperator redis;

    public static final String REQUEST_LIMIT_IP = "REQUEST:LIMIT:FOR";

    public static final String MOBILE_VERIFY_CODE = "VERIFY:CODE";

    public static final Integer MINIMUM_REQUEST_TIME_SPAN_SECONDS = 60;

    // Verify code timeout after 30 min
    public static final Integer VERIFY_CODE_TIMEOUT_SECONDS = 1800;
}

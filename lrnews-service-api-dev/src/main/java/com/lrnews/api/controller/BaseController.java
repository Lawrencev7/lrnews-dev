package com.lrnews.api.controller;

import com.lrnews.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;


public class BaseController {
    @Autowired
    protected RedisOperator redis;

}

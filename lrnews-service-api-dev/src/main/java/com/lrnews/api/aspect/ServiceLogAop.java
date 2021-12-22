package com.lrnews.api.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class ServiceLogAop {
    final static Logger logger = LoggerFactory.getLogger(ServiceLogAop.class);
}
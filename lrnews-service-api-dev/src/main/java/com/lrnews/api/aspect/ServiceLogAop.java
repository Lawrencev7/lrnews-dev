package com.lrnews.api.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class ServiceLogAop {
    private final static Logger logger = LoggerFactory.getLogger(ServiceLogAop.class);

    @Around("execution(* com.lrnews.*.service.impl.*.*(..))")
    public Object logExecTime(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("==> AOP logger start ====> {}.{} ", joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName());
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();

        long time = end - start;
        String info = "==> AOP logger end ====> Current function takes: " + time + "ms";
        if (time > 300) {
            logger.warn(info);
        } else {
            logger.info(info);
        }

        return result;
    }
}
package com.lrnews.api.config;

import com.lrnews.api.interceptors.PassportInterceptor;
import com.lrnews.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    final RedisOperator redis;

    public InterceptorConfig(RedisOperator redis) {
        this.redis = redis;
    }

    @Bean
    public PassportInterceptor getPassportInterceptor(){
        return new PassportInterceptor(redis);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getPassportInterceptor()).addPathPatterns("/verify/get-verify-code");
    }


}

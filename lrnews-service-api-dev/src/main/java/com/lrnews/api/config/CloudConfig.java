package com.lrnews.api.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;

@Configuration
public class CloudConfig {

    public CloudConfig() {
    }

    @Bean
    @LoadBalanced
    public RestOperations restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}

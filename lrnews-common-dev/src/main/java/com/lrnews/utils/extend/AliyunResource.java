package com.lrnews.utils.extend;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:/aliyun.properties")
@ConfigurationProperties(prefix = "aliyun")
public class AliyunResource {
    private String accessKeyId;

    private String accessSecret;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public String getAccessSecret() {
        return accessSecret;
    }
}

package com.lrnews.utils.extend;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aliyun")
public class AliyunResource {
    private String accessKeyId="123";

    private String accessSecret="test";

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }
}

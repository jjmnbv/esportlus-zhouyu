package com.kaihei.esportingplus.auth.server.config;

import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnExpression("!'${oauth}'.isEmpty()")
@ConfigurationProperties(prefix = "oauth")
public class OauthUrlsConfig {

    private List<String> urls;

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}

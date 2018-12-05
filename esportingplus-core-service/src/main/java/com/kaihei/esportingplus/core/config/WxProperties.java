package com.kaihei.esportingplus.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author liuyang
 * @Description
 * @Date 2018/11/8 15:06
 **/
@Configuration
public class WxProperties {

    @Value("${wx.access.token.url}")
    private String tokenUrl;

    @Value("${wx.service.notify.url}")
    private String norifyUrl;

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.secret}")
    private String secret;

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public String getNorifyUrl() {
        return norifyUrl;
    }

    public void setNorifyUrl(String norifyUrl) {
        this.norifyUrl = norifyUrl;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}

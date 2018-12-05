package com.kaihei.esportingplus.core.config;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import io.rong.RongCloud;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(value = "message")
public class MessageConfig {

    private JiGuang jiGuang;
    private Ronyun ronyun;
    private Integer operator;

    public static class JiGuang {
        private String appKey;
        private String appSecret;

        public String getAppKey() {
            return appKey;
        }

        public void setAppKey(String appKey) {
            this.appKey = appKey;
        }

        public String getAppSecret() {
            return appSecret;
        }

        public void setAppSecret(String appSecret) {
            this.appSecret = appSecret;
        }
    }

    public static class Ronyun {
        private String appKey;
        private String appSecret;
        private String imSecretKey;
        private String url;
        private String imgurl;

        public String getAppKey() {
            return appKey;
        }

        public void setAppKey(String appKey) {
            this.appKey = appKey;
        }

        public String getAppSecret() {
            return appSecret;
        }

        public void setAppSecret(String appSecret) {
            this.appSecret = appSecret;
        }

        public String getImSecretKey() {
            return imSecretKey;
        }

        public void setImSecretKey(String imSecretKey) {
            this.imSecretKey = imSecretKey;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }
    }


    public JiGuang getJiGuang() {
        return jiGuang;
    }

    public void setJiGuang(JiGuang jiGuang) {
        this.jiGuang = jiGuang;
    }

    public Ronyun getRonyun() {
        return ronyun;
    }

    public void setRonyun(Ronyun ronyun) {
        this.ronyun = ronyun;
    }

    public Integer getOperator() {
        return operator;
    }

    public void setOperator(Integer operator) {
        this.operator = operator;
    }

    @Bean
    public RongCloud rongCloud() {
        return RongCloud.getInstance(ronyun.appKey, ronyun.appSecret);
    }

//    @Bean
//    public JPushClient jPushClient() {
//        JPushClient jpushClient = new JPushClient(jiGuang.appSecret, jiGuang.appKey, null, ClientConfig.getInstance());
//        return jpushClient;
//    }

}

package com.kaihei.esportingplus.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author liuyang
 * @Description
 * @Date 2018/10/25 10:59
 **/
@Configuration
@ConfigurationProperties("sms.channel")
public class SmsProperties {

    private String prefix;
    private SmsProperties.Notification notification = new SmsProperties.Notification();
    private SmsProperties.Promotion Promotion = new SmsProperties.Promotion();
    public static class SMSInfo{
        protected String account;
        protected String password;
        protected String send_url;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getSend_url() {
            return send_url;
        }

        public void setSend_url(String send_url) {
            this.send_url = send_url;
        }
    }

    public static class Notification extends SMSInfo{
    }

    public static class Promotion extends SMSInfo{
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public SmsProperties.Promotion getPromotion() {
        return Promotion;
    }

    public void setPromotion(SmsProperties.Promotion promotion) {
        Promotion = promotion;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}

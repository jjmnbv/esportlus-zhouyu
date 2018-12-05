package com.kaihei.esportingplus.riskrating.api.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 用户注册登陆设备信息Vo类
 * @author chenzhenjun
 */
public class UserDeviceBehaviourRecordVo implements Serializable {


    private static final long serialVersionUID = -4181082265948899925L;
    private String userId;

    private String registerDeviceId;

    private String userAgent;

    private String channel;

    private String rcloudToken;

    private Integer platform;

    private String loginDeviceId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRegisterDeviceId() {
        return registerDeviceId;
    }

    public void setRegisterDeviceId(String registerDeviceId) {
        this.registerDeviceId = registerDeviceId;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getRcloudToken() {
        return rcloudToken;
    }

    public void setRcloudToken(String rcloudToken) {
        this.rcloudToken = rcloudToken;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public String getLoginDeviceId() {
        return loginDeviceId;
    }

    public void setLoginDeviceId(String loginDeviceId) {
        this.loginDeviceId = loginDeviceId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

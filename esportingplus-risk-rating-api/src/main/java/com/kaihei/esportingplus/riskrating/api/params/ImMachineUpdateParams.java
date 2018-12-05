package com.kaihei.esportingplus.riskrating.api.params;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 登陆注册风控入参
 * @author chenzhenjun
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Validated
public class ImMachineUpdateParams implements Serializable {

    private static final long serialVersionUID = 8103277723173992177L;
    @NotNull(message = "调用类型不能为空")
    private String type;

    @NotNull(message = "用户ID不能为空")
    private String userId;

    @NotNull(message = "数美ID不能为空")
    private String deviceId;

    private String userAgent;

    private String channel;

    /**
     * 融云token
     */
    private String rcloudToken;

    /**
     * 手机系统
     */
//    @NotNull(message = "手机系统不能为空")
    private String platform;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 标识来源渠道号
     */
    private String version;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

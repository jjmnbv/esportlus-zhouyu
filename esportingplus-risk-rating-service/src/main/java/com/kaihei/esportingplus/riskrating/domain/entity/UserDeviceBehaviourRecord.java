package com.kaihei.esportingplus.riskrating.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * 用户设备行为记录
 * @author chenzhenjun
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_user_id", columnNames = "userId")},
        indexes = {@Index(name = "idx_register_device_id", columnList = "registerDeviceId"),
                @Index(name = "idx_login_device_id", columnList = "loginDeviceId")
        })
public class UserDeviceBehaviourRecord extends AbstractEntity{

    /**
     * 用户id
     */
    @Column(nullable = false, columnDefinition = "varchar(10) COMMENT '用户id'")
    private String userId;

    /**
     * 注册数美id
     */
    @Column(nullable = false, columnDefinition = "varchar(190) COMMENT '注册数美id'")
    private String registerDeviceId;

    /**
     * 设备信息
     */
    @Column(columnDefinition = "varchar(256) DEFAULT NULL COMMENT '设备信息'")
    private String userAgent;

    /**
     * 应用渠道
     */
    @Column(columnDefinition = "varchar(32) DEFAULT NULL COMMENT '应用渠道'")
    private String channel;

    /**
     * 融云token
     */
    @Column(columnDefinition = "varchar(128) DEFAULT NULL COMMENT '融云token'")
    private String rcloudToken;

    /**
     * 手机系统
     */
    @Column(nullable = false, columnDefinition = "int(5) COMMENT '手机系统：IOS-1，ANDROID-2'")
    private Integer platform;

    /**
     * 登陆数美ID
     * 解绑清空该字段
     */
    @Column(columnDefinition = "varchar(190) DEFAULT NULL COMMENT '登陆数美id'")
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
        return ToStringBuilder.reflectionToString(this);
    }
}

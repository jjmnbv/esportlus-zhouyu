package com.kaihei.esportingplus.user.domain.entity;

import javax.persistence.*;

@Table(name = "members_registereddevice")
public class MembersRegisteredDevice {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 注册设备ID
     */
    @Column(name = "device_id")
    private String deviceId;

    /**
     * 设备信息
     */
    @Column(name = "user_agent")
    private String userAgent;

    /**
     * 用户user_id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 渠道号
     */
    private String channel;

    /**
     * 融云token
     */
    @Column(name = "rcloud_token")
    private String rcloudToken;

    /**
     * 手机系统：IOS-1，ANDROID-2
     */
    private Integer platform;

    /**
     * 登录设备ID
     */
    @Column(name = "login_device_id")
    private String loginDeviceId;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取注册设备ID
     *
     * @return device_id - 注册设备ID
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * 设置注册设备ID
     *
     * @param deviceId 注册设备ID
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    /**
     * 获取设备信息
     *
     * @return user_agent - 设备信息
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * 设置设备信息
     *
     * @param userAgent 设备信息
     */
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent == null ? null : userAgent.trim();
    }

    /**
     * 获取用户user_id
     *
     * @return user_id - 用户user_id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户user_id
     *
     * @param userId 用户user_id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取渠道号
     *
     * @return channel - 渠道号
     */
    public String getChannel() {
        return channel;
    }

    /**
     * 设置渠道号
     *
     * @param channel 渠道号
     */
    public void setChannel(String channel) {
        this.channel = channel == null ? null : channel.trim();
    }

    /**
     * 获取融云token
     *
     * @return rcloud_token - 融云token
     */
    public String getRcloudToken() {
        return rcloudToken;
    }

    /**
     * 设置融云token
     *
     * @param rcloudToken 融云token
     */
    public void setRcloudToken(String rcloudToken) {
        this.rcloudToken = rcloudToken == null ? null : rcloudToken.trim();
    }

    /**
     * 获取手机系统：IOS-1，ANDROID-2
     *
     * @return platform - 手机系统：IOS-1，ANDROID-2
     */
    public Integer getPlatform() {
        return platform;
    }

    /**
     * 设置手机系统：IOS-1，ANDROID-2
     *
     * @param platform 手机系统：IOS-1，ANDROID-2
     */
    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    /**
     * 获取登录设备ID
     *
     * @return login_device_id - 登录设备ID
     */
    public String getLoginDeviceId() {
        return loginDeviceId;
    }

    /**
     * 设置登录设备ID
     *
     * @param loginDeviceId 登录设备ID
     */
    public void setLoginDeviceId(String loginDeviceId) {
        this.loginDeviceId = loginDeviceId == null ? null : loginDeviceId.trim();
    }
}
package com.kaihei.esportingplus.riskrating.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 免费车队-风控服务-配置参数
 * @author chenzhenjun
 */
@Entity
public class FreeTeamDeviceBind extends AbstractEntity{

    /***
     * 用户id
     */
    @Column(nullable = false, columnDefinition = "varchar(128) COMMENT '用户id'")
    private String uid;

    /**
     * 数美id
     */
    @Column(nullable = false, columnDefinition = "varchar(128) COMMENT '数美id'")
    private String deviceId;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

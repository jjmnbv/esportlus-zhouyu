package com.kaihei.esportingplus.riskrating.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 免费车队-恶意设备白名单记录表
 * @author chenzhenjun
 */
@Entity
public class FreeTeamDeviceWhite extends AbstractEntity{

    /**
     * 数美id
     */
    @Column(nullable = false, columnDefinition = "varchar(128) COMMENT '数美id'")
    private String deviceId;

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

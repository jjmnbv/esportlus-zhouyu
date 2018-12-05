package com.kaihei.esportingplus.riskrating.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * IM防骚扰及虚拟机判断需求-数美设备黑名单表
 * @author chenzhenjun
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_device_id", columnNames = "deviceId")})
public class ImMachineDeviceBlack extends AbstractEntity{

    /**
     * 数美id
     */
    @Column(nullable = false, columnDefinition = "varchar(190) COMMENT '数美id'")
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

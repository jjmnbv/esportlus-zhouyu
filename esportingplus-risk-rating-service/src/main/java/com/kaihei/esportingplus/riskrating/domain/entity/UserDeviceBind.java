package com.kaihei.esportingplus.riskrating.domain.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 账户数美绑定关系
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_bind_user_id_device_id",
        columnNames = {"userId", "deviceId"})})
public class UserDeviceBind extends AbstractEntity{

    @Column(nullable = false, columnDefinition = "varchar(8) COMMENT '用户id'")
    private String userId;

    @Column(nullable = false, columnDefinition = "varchar(190) COMMENT '数美id'")
    private String deviceId;

    /**
     * 绑定状态(0-已解绑 1-绑定)
     */
    @Column(nullable = false, columnDefinition = "int(1) COMMENT '绑定状态(0-已解绑 1-绑定)'")
    private Integer bindStatus;

    /**
     * 绑定时间
     */
    @Column(columnDefinition = "datetime DEFAULT NULL COMMENT '绑定时间'")
    private Date bindTime;

    /**
     * 解绑时间
     */
    @Column(columnDefinition = "datetime DEFAULT NULL COMMENT '解绑时间'")
    private Date unbindTime;

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

    public Integer getBindStatus() {
        return bindStatus;
    }

    public void setBindStatus(Integer bindStatus) {
        this.bindStatus = bindStatus;
    }

    public Date getBindTime() {
        return bindTime;
    }

    public void setBindTime(Date bindTime) {
        this.bindTime = bindTime;
    }

    public Date getUnbindTime() {
        return unbindTime;
    }

    public void setUnbindTime(Date unbindTime) {
        this.unbindTime = unbindTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

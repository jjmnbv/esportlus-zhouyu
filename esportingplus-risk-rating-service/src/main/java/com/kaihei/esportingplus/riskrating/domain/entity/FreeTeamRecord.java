package com.kaihei.esportingplus.riskrating.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 免费车队——使用记录
 * @author chenzhenjun
 */
@Entity
public class FreeTeamRecord implements Serializable {

    private static final long serialVersionUID = 1377835422947076128L;

    /**
     * entity id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

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

    @Column(columnDefinition = "datetime DEFAULT NULL COMMENT '车队结束时间'")
    private LocalDateTime endDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

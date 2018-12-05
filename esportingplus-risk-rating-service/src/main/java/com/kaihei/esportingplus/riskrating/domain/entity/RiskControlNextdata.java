package com.kaihei.esportingplus.riskrating.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.Date;

/**
 * 数美风控记录表
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_uid_device_id_action", columnNames = {"uid","deviceId","action"})},
        indexes = {@Index(name = "idx_device_id", columnList = "deviceId")})
public class RiskControlNextdata {

    /**
     * entity id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(8) COMMENT '用户id'")
    private String uid;

    @Column(nullable = false, columnDefinition = "varchar(190) COMMENT '数美id'")
    private String deviceId;

    @Column(nullable = false, columnDefinition = "int(10) COMMENT '调用数美API类型'")
    private int action;

    @Column(nullable = false, columnDefinition = "int(10) COMMENT '数美风险分'")
    private int riskScore;

    @Column(nullable = false, columnDefinition = "varchar(64) COMMENT '数美风险等级'")
    private String riskLevel;

    @Column(nullable = false, columnDefinition = "varchar(512) COMMENT '数美风险详情'")
    private String riskDetail;

    @Column(columnDefinition = "longtext COMMENT '数美返回完整信息'")
    private String source;

    @Column(columnDefinition = "datetime DEFAULT NULL COMMENT '更新时间'")
    private Date updateDatetime;

    @Column(columnDefinition = "datetime DEFAULT NULL COMMENT '创建时间'")
    private Date createDatetime;

    public RiskControlNextdata() {
    }

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

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(int riskScore) {
        this.riskScore = riskScore;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getRiskDetail() {
        return riskDetail;
    }

    public void setRiskDetail(String riskDetail) {
        this.riskDetail = riskDetail;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

package com.kaihei.esportingplus.user.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 数美风控记录表
 */
@Table(name = "risk_control_nextdata")
public class RiskControlNextdata {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name = "uid")
    private String uid;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "action")
    private int action;

    @Column(name = "risk_score")
    private int riskScore;

    @Column(name = "risk_level")
    private String riskLevel;

    @Column(name = "risk_detail")
    private String riskDetail;

    @Column(name = "source")
    private String source;

    @Column(name = "update_datetime")
    private Date updateTime;

    @Column(name = "create_datetime")
    private Date createTime;

    public RiskControlNextdata(Integer id, String uid, String deviceId, int action, int riskScore, String riskLevel,
                               String riskDetail, String source, Date updateTime, Date createTime) {
        this.id = id;
        this.uid = uid;
        this.deviceId = deviceId;
        this.action = action;
        this.riskScore = riskScore;
        this.riskLevel = riskLevel;
        this.riskDetail = riskDetail;
        this.source = source;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }

    public RiskControlNextdata() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

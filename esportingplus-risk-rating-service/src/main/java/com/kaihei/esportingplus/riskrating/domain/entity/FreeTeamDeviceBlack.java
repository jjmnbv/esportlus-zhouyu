package com.kaihei.esportingplus.riskrating.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 免费车队-恶意设备记录表
 */
@Entity
public class FreeTeamDeviceBlack extends AbstractEntity{

    /***
     * 用户id
     */
    @Column(nullable = false, columnDefinition = "varchar(128) COMMENT '用户id'")
    private String uid;

    /***
     * 用户昵称
     */
    @Column(nullable = false, columnDefinition = "varchar(128) COMMENT '用户昵称'")
    private String nickName;

    /***
     * 鸡牌号
     */
    @Column(nullable = false, columnDefinition = "int(11) COMMENT '鸡牌号'")
    private int chickenId;

    /**
     * 数美id
     */
    @Column(nullable = false, columnDefinition = "varchar(128) COMMENT '数美id'")
    private String deviceId;

    /**
     * 数美风险分
     */
    @Column(nullable = false, columnDefinition = "varchar(128) COMMENT '数美风险分'")
    private int riskScore;

    /**
     * 数美风险详情
     */
    @Column(nullable = false, columnDefinition = "varchar(128) COMMENT '数美风险详情'")
    private String riskDetail;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getChickenId() {
        return chickenId;
    }

    public void setChickenId(int chickenId) {
        this.chickenId = chickenId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(int riskScore) {
        this.riskScore = riskScore;
    }

    public String getRiskDetail() {
        return riskDetail;
    }

    public void setRiskDetail(String riskDetail) {
        this.riskDetail = riskDetail;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

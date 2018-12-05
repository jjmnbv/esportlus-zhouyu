package com.kaihei.esportingplus.riskrating.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 免费车队-数美id上车次数统计
 * @author chenzhenjun
 */
@Entity
public class FreeTeamChance extends AbstractEntity{

    /**
     * 数美id
     */
    @Column(nullable = false, columnDefinition = "varchar(128) COMMENT '数美id'")
    private String deviceId;

    /**
     * 数美id现存已用次数
     */
    @Column(nullable = false, columnDefinition = "int(5) COMMENT '数美ID现存已用次数'")
    private int freeChanceUsed;

    /**
     * 数美ID当天可用次数
     */
    @Column(nullable = false, columnDefinition = "int(5) COMMENT '数美ID当天可用次数'")
    private int freeChanceRemain;

    @Column(nullable = false, columnDefinition = "varchar(10) COMMENT '统计日期(YYYY-MM-DD)'")
    private String statisticsDate;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getFreeChanceUsed() {
        return freeChanceUsed;
    }

    public void setFreeChanceUsed(int freeChanceUsed) {
        this.freeChanceUsed = freeChanceUsed;
    }

    public int getFreeChanceRemain() {
        return freeChanceRemain;
    }

    public void setFreeChanceRemain(int freeChanceRemain) {
        this.freeChanceRemain = freeChanceRemain;
    }

    public String getStatisticsDate() {
        return statisticsDate;
    }

    public void setStatisticsDate(String statisticsDate) {
        this.statisticsDate = statisticsDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

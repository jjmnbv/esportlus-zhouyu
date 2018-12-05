package com.kaihei.esportingplus.riskrating.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 免费车队-风控服务-配置参数
 * @author chenzhenjun
 */
@Entity
public class FreeTeamConfig extends AbstractEntity {

    /***
     * 模块code
     */
    @Column(nullable = false, columnDefinition = "varchar(128) COMMENT '风控模块code'")
    private String moduleCode;

    /***
     * 免费上车机会每日次数
     */
    @Column(nullable = false, columnDefinition = "int(10) COMMENT '免费上车机会每日次数'")
    private int freeDayCount;

    /***
     * 免费上车机会每周次数
     */
    @Column(nullable = false, columnDefinition = "int(10) COMMENT '免费上车机会每周次数'")
    private int freeWeekCount;

    /***
     * 恶意设备风险分临界值
     */
    @Column(nullable = false, columnDefinition = "int(10) COMMENT '恶意设备风险分临界值'")
    private int riskScoreLimit;

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public int getFreeDayCount() {
        return freeDayCount;
    }

    public void setFreeDayCount(int freeDayCount) {
        this.freeDayCount = freeDayCount;
    }

    public int getFreeWeekCount() {
        return freeWeekCount;
    }

    public void setFreeWeekCount(int freeWeekCount) {
        this.freeWeekCount = freeWeekCount;
    }

    public int getRiskScoreLimit() {
        return riskScoreLimit;
    }

    public void setRiskScoreLimit(int riskScoreLimit) {
        this.riskScoreLimit = riskScoreLimit;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

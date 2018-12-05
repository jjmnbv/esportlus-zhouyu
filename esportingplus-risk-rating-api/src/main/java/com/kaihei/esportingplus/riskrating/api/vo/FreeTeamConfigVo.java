package com.kaihei.esportingplus.riskrating.api.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * 免费车队风控配置业务Vo类
 * @author chenzhenjun
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FreeTeamConfigVo implements Serializable {

    private static final long serialVersionUID = 7574656632797732553L;

    private long id;

    private String moduleCode;

    /***
     * 免费上车机会每日次数
     */
    private int freeDayCount;

    /***
     * 免费上车机会每周次数
     */
    private int freeWeekCount;

    /***
     * 恶意设备风险分临界值
     */
    private int riskScoreLimit;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
}

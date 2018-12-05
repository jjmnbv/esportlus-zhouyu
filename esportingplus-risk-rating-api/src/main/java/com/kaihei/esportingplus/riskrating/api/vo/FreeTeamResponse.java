package com.kaihei.esportingplus.riskrating.api.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 免费车队——风控返回实体类
 * @author chenzhenjun
 */
public class FreeTeamResponse {

    /**
     * 编码
     */
    private String riskCode;
    /**
     * 说明
     */
    private String riskMsg;

    public FreeTeamResponse() { }

    public FreeTeamResponse(String riskCode, String riskMsg) {
        this.riskCode = riskCode;
        this.riskMsg = riskMsg;
    }

    public String getRiskCode() {
        return riskCode;
    }

    public void setRiskCode(String riskCode) {
        this.riskCode = riskCode;
    }

    public String getRiskMsg() {
        return riskMsg;
    }

    public void setRiskMsg(String riskMsg) {
        this.riskMsg = riskMsg;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

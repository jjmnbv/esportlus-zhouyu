package com.kaihei.esportingplus.riskrating.api.vo;

import com.kaihei.esportingplus.riskrating.api.enums.RiskEnum;

public class RiskBaseResponse  {
    /**
     * 编码
     */
    private String riskCode;
    /**
     * 说明
     */
    private String riskMsg;

    private boolean rechargeFlag;

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

    public boolean isRechargeFlag() {
        return rechargeFlag;
    }

    public void setRechargeFlag(boolean rechargeFlag) {
        this.rechargeFlag = rechargeFlag;
    }


    /**
     * 风险返回
     */
    public static RiskBaseResponse risk(RiskEnum riskEnum) {
        RiskBaseResponse riskBaseResponse = new RiskBaseResponse();
        riskBaseResponse.setRiskCode(riskEnum.getCode());
        riskBaseResponse.setRiskMsg(riskEnum.getMarkWords());
        riskBaseResponse.setRechargeFlag(false);
        return riskBaseResponse;
    }

    /**
     * 正常返回
     */
    public static RiskBaseResponse normal() {
        RiskBaseResponse riskBaseResponse = new RiskBaseResponse();
        riskBaseResponse.setRiskCode(RiskEnum.NOMARL.getCode());
        riskBaseResponse.setRiskMsg(RiskEnum.NOMARL.getMarkWords());
        riskBaseResponse.setRechargeFlag(true);
        return riskBaseResponse;
    }
}

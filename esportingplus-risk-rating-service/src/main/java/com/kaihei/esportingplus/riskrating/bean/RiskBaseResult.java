package com.kaihei.esportingplus.riskrating.bean;

import com.kaihei.esportingplus.riskrating.api.enums.RiskEnum;

public class RiskBaseResult {

    private RiskEnum riskEnum;

    private boolean rechargeFlag;

    public RiskEnum getRiskEnum() {
        return riskEnum;
    }

    public void setRiskEnum(RiskEnum riskEnum) {
        this.riskEnum = riskEnum;
    }

    public boolean isRechargeFlag() {
        return rechargeFlag;
    }

    public void setRechargeFlag(boolean rechargeFlag) {
        this.rechargeFlag = rechargeFlag;
    }
}

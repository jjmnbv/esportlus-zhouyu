package com.kaihei.esportingplus.riskrating.bean;

public class RiskRechargeResult {
    private RiskDeviceUserBindResult riskDeviceUserBindResult;
    private BlackAmountResult blackAmountResult;

    public RiskDeviceUserBindResult getRiskDeviceUserBindResult() {
        return riskDeviceUserBindResult;
    }

    public void setRiskDeviceUserBindResult(
            RiskDeviceUserBindResult riskDeviceUserBindResult) {
        this.riskDeviceUserBindResult = riskDeviceUserBindResult;
    }

    public BlackAmountResult getBlackAmountResult() {
        return blackAmountResult;
    }

    public void setBlackAmountResult(
            BlackAmountResult blackAmountResult) {
        this.blackAmountResult = blackAmountResult;
    }
}

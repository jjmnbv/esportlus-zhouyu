package com.kaihei.esportingplus.riskrating.bean;

import com.kaihei.esportingplus.riskrating.domain.entity.RiskDeviceUserRechargeBind;

public class RiskDeviceUserBindResult extends RiskBaseResult {
    private boolean isSave;
    private RiskDeviceUserRechargeBind riskDeviceUserRechargeBind;
    private boolean isWhite;
    public boolean isSave() {
        return isSave;
    }

    public void setSave(boolean save) {
        isSave = save;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setWhite(boolean white) {
        isWhite = white;
    }

    public RiskDeviceUserRechargeBind getRiskDeviceUserRechargeBind() {
        return riskDeviceUserRechargeBind;
    }

    public void setRiskDeviceUserRechargeBind(
            RiskDeviceUserRechargeBind riskDeviceUserRechargeBind) {
        this.riskDeviceUserRechargeBind = riskDeviceUserRechargeBind;
    }
}

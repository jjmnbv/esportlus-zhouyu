package com.kaihei.esportingplus.riskrating.bean;

public class BlackAmountResult extends RiskBaseResult {
    private boolean smallFlag;
    /**
     * 当前小时
     */
    private String currentHour;
    /**
     * 当前天
     */
    private String currentDay;

    /**
     * 充值次数
     */
    private Integer rechargeCount;
    /**
     * 阈值次数
     */
    private Integer threshold;
    public boolean isSmallFlag() {
        return smallFlag;
    }

    public void setSmallFlag(boolean smallFlag) {
        this.smallFlag = smallFlag;
    }

    public String getCurrentHour() {
        return currentHour;
    }

    public void setCurrentHour(String currentHour) {
        this.currentHour = currentHour;
    }

    public String getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(String currentDay) {
        this.currentDay = currentDay;
    }

    public Integer getRechargeCount() {
        return rechargeCount;
    }

    public void setRechargeCount(Integer rechargeCount) {
        this.rechargeCount = rechargeCount;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }
}

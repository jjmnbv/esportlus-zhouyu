package com.kaihei.esportingplus.riskrating.bean;

public class BlackAmountTemCache extends RiskBaseResult {
    /**
     * 当前设备今日累计充值次数
     */
    private int deviceDayRechargeCount;
    /**
     * 当前设备本小时累计充值次数
     */
    private int deviceHourRechargeCount;
    /**
     * 当前用户今日累计充值次数
     */
    private int userDayRechargeCount;
    /**
     * 当前用户本小时累计充值次数
     */
    private int userHourRechargeCount;



    public int getDeviceDayRechargeCount() {
        return deviceDayRechargeCount;
    }

    public void setDeviceDayRechargeCount(int deviceDayRechargeCount) {
        this.deviceDayRechargeCount = deviceDayRechargeCount;
    }

    public int getDeviceHourRechargeCount() {
        return deviceHourRechargeCount;
    }

    public void setDeviceHourRechargeCount(int deviceHourRechargeCount) {
        this.deviceHourRechargeCount = deviceHourRechargeCount;
    }

    public int getUserDayRechargeCount() {
        return userDayRechargeCount;
    }

    public void setUserDayRechargeCount(int userDayRechargeCount) {
        this.userDayRechargeCount = userDayRechargeCount;
    }

    public int getUserHourRechargeCount() {
        return userHourRechargeCount;
    }

    public void setUserHourRechargeCount(int userHourRechargeCount) {
        this.userHourRechargeCount = userHourRechargeCount;
    }

}

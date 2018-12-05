package com.kaihei.esportingplus.riskrating.api.enums;

public enum RiskDictEnum {
    DEVICE_USER_BIND_COUNT("BLACK_RESIST","DEVICE_USER_BIND_COUNT","黑产防刷组-设备用户绑定数量"),
    SMALL_MONEY_CHECK_AMOUNT("BLACK_RESIST","SMALL_MONEY_CHECK_AMOUNT","黑产防刷组-小于或等于多少金额进行小金额次数校验"),
    DEVICE_HOUR_RECHARGE_COUNT("BLACK_RESIST","DEVICE_HOUR_RECHARGE_COUNT","黑产防刷组-设备小时充值次数"),
    DEVICE_DAY_RECHARGE_COUNT("BLACK_RESIST","DEVICE_DAY_RECHARGE_COUNT","黑产防刷组-设备每日充值次数"),
    RECHARGE_CURRENCY_TYPE("BLACK_RESIST","RECHARGE_CURRENCY_TYPE","黑产防刷组-货币充值类型"),
    USER_HOUR_RECHARGE_COUNT("BLACK_RESIST","USER_HOUR_RECHARGE_COUNT","黑产防刷组-用户小时充值次数"),
    USER_DAY_RECHARGE_COUNT("BLACK_RESIST","USER_DAY_RECHARGE_COUNT","黑产防刷组-用户小时充值次数"),
    RISK_SWITCH("RISK_SWITCH","RISK_SWITCH","风控开关-风控开关0:关闭,1开启");
    /**
     * 组编码
     */
    private String groupCode;
    /**
     * 单项编码
     */
    private String itemCode;
    /**
     * 说明
     */
    private String desc;


    RiskDictEnum(String groupCode, String itemCode, String desc) {
        this.groupCode = groupCode;
        this.itemCode = itemCode;
        this.desc = desc;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public String getDesc() {
        return desc;
    }
}

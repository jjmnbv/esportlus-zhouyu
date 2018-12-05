package com.kaihei.esportingplus.riskrating.api.enums;

public enum RiskEnum {
    NOMARL("NOMARL", "正常", "", "允许充值"),
    DEVICE_USER_BIND_LIMIT("DEVICE_USER_BIND_LIMIT", "设备账号绑定超过限制", "风控预警提示：设备号[%s]绑定的充值账号超过阈值",
            "您的充值账号已达上限，如需充值请联系客服。"),
    DEVICE_DAY_RECHARGE_LIMIT("DEVICE_DAY_RECHARGE_LIMIT", "设备今日充值已达上限",
            "风控预警提示：设备号[%s]今日小额充值[%d]次,已超过阈值[%d]次", "您的充值次数已达上限，请关注微信公众号“暴鸡电竞”进行优惠充值。"),
    DEVICE_HOUR_RECHARGE_LIMIT("DEVICE_HOUR_RECHARGE_LIMIT", "设备本时充值已达上限",
            "风控预警提示：设备号[%s]在今日[%s]时小额充值[%d]次,已超过阈值[%d]次", "您的充值次数已达上限，请关注微信公众号“暴鸡电竞”进行优惠充值。"),
    USER_DAY_RECHARGE_LIMIT("USER_DAY_RECHARGE_LIMIT", "用户今日充值已达上限",
            "风控预警提示：用户[%s]今日小额充值[%d]次,已超过阈值[%d]次", "您的充值次数已达上限，请关注微信公众号“暴鸡电竞”进行优惠充值。"),
    USER_HOUR_RECHARGE_LIMIT("USER_HOUR_RECHARGE_LIMIT", "用户本时充值已达上限",
            "风控预警提示：用户[%s]在今日[%s]时小额充值[%d]次,已超过阈值[%d]次", "您的充值次数已达上限，请关注微信公众号“暴鸡电竞”进行优惠充值。"),

    RECHARGE_CURRENCY_ILLEGAL("RECHARGE_CURRENCY_ILLEGAL", "充值货币类型非法",
            "风控预警提示：用户[%s]充值货币类型非法，其充值的货币类型为[%s]", "请使用人民币进行充值"),


    USER_DAY_RECHARGE_AMOUNT_LIMIT("USER_RECHARGE_AMOUNT_LIMIT", "用户日充值已达限额",
            null,
            "您已达当日苹果充值金额上限，请关注微信公众号“暴鸡电竞”进行优惠充值。"),
    USER_MONTH_RECHARGE_AMOUNT_LIMIT("USER_RECHARGE_AMOUNT_LIMIT", "用户月充值已达限额",
            null,
            "您已达当月苹果充值金额上限，请关注微信公众号“暴鸡电竞”进行优惠充值。"),

    DEVICE_DAY_RECHARGE_AMOUNT_LIMIT("DEVICE_RECHARGE_AMOUNT_LIMIT", "设备日充值已达限额",
            null, "您已达当日苹果充值金额上限，请关注微信公众号“暴鸡电竞”进行优惠充值。"),
    DEVICE_MONTH_RECHARGE_AMOUNT_LIMIT("DEVICE_RECHARGE_AMOUNT_LIMIT", "设备月充值已达限额",
            null, "您已达当月苹果充值金额上限，请关注微信公众号“暴鸡电竞”进行优惠充值。"),


    PLATFORM_IOS_RECHARGE_FORBIDDEN("PLATFORM_IOS_RECHARGE_FORBIDDEN", "平台IOS充值被禁", null,
            "请关注微信公众号“暴鸡电竞”进行优惠充值。"),
    USER_RECHARGE_FREEZED("USER_RECHARGE_FREEZED", "您的充值功能已被冻结，如需解冻请联系客服。", null, "您的充值功能已被冻结，如需解冻请联系客服。"),

    ;
    /**
     * 组编码
     */
    private String code;
    /**
     * 说明
     */
    private String desc;
    /**
     * 告警信息
     */
    private String alermFormat;
    /**
     * 前端提示语
     */
    private String markWords;

    RiskEnum(String code, String desc, String alermFormat, String markWords) {
        this.code = code;
        this.desc = desc;
        this.alermFormat = alermFormat;
        this.markWords = markWords;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getAlermFormat() {
        return alermFormat;
    }

    public String getMarkWords() {
        return markWords;
    }
}

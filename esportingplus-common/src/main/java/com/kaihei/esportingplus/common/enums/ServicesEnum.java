package com.kaihei.esportingplus.common.enums;

/**
 * 服务名枚举
 *
 * @author Orochi-Yzh
 * @dateTime 2018/1/24 18:19
 * @updatetor
 */
public enum ServicesEnum {
    AUTH_SERVER("esportingplus-auth-server", "统一鉴权务"),
    GAMINGTEAM_SERVICE("esportingplus-gamingteam-service", "游戏车队服务"),
    TRADE_SERVICE("esportingplus-trade-service", "订单服务"),
    RESOURCE_SERVICE("esportingplus-resource-service", "资源服务"),
    USER_SERVICE("esportingplus-user-service", "用户服务"),
    PAYMENT_SERVICE("esportingplus-payment-service", "支付服务"),
    MARKETING_SERVICE("esportingplus-marketing-service", "营销服务"),
    RISK_RATING_SERVICE("esportingplus-risk-rating-service", "风控服务"),
    CORE_SERVICE("esportingplus-core-service", "风控服务"),
    UNKNOWN("UNKNOWN", "未知服务");

    private String code;
    private String desc;

    ServicesEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ServicesEnum fromCode(String code) {
        for (ServicesEnum c : ServicesEnum.values()) {
            if (c.code.equals(code)) {
                return c;
            }
        }
        return UNKNOWN;
    }

    public static ServicesEnum fromDesc(String desc) {
        for (ServicesEnum c : ServicesEnum.values()) {
            if (c.desc.equals(desc)) {
                return c;
            }
        }
        return UNKNOWN;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
package com.kaihei.esportingplus.gamingteam.api.enums;

/**
 * 老板支付结果枚举
 * @author liangyi
 */
public enum BossPaymentResultEnum {

    /** 支付失败 */
    FAILED(0, "支付失败"),

    /** 支付成功 */
    SUCCESS(1, "支付成功");

    private int code;
    private String msg;

    BossPaymentResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
    
}

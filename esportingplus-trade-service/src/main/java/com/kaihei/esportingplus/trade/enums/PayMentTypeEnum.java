package com.kaihei.esportingplus.trade.enums;

/**
 * 支付类型, 0: 微信 1: 支付宝 2:QQ钱包 3: 我的钱包
 */
public enum PayMentTypeEnum {
    /**微信*/
    WEIXIN_PAY(0,"微信"),
    /**支付宝*/
    ZFB_PAY(1,"支付宝"),
    /**QQ钱包*/
    QQ_PAY(2,"QQ钱包"),
    /**我的钱包*/
    WALLET_PAY(3,"我的钱包");

    private int code;
    private String msg;

    PayMentTypeEnum(int code, String msg) {
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

package com.kaihei.esportingplus.payment.api.enums;

/**
 * @program: esportingplus-payment
 * @description: 状态码(AVAILABLE表示可用 、 FROZEN表示冻结 、 UNAVAILABLE表示不可用)
 * @author: xusisi
 * @create: 2018-08-18 11:29
 **/
public enum AccountType {

    /**
     * 暴鸡币账户
     */
    GCOIN_ACCOUNT("001", "暴鸡币账户"),

    /**
     * 暴击值账户
     */
    STARLIGHT_ACCOUNT("002", "暴击值账户");

    private String code;
    private String msg;

    AccountType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

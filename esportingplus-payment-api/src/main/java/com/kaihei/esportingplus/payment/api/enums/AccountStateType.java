package com.kaihei.esportingplus.payment.api.enums;

/**
 * @program: esportingplus-payment
 * @description: 状态码(AVAILABLE表示可用、FROZEN表示冻结、UNAVAILABLE表示不可用)
 * @author: xusisi
 * @create: 2018-08-18 11:29
 **/
public enum AccountStateType {
    /**
     * AVAILABLE表示可用
     */
    AVAILABLE("AVAILABLE","可用"),

    /**
     * FROZEN表示冻结
     */
    FROZEN("FROZEN","冻结"),

    /**
     * UNAVAILABLE表示不可用
     */
    UNAVAILABLE("UNAVAILABLE","不可用");

    private String code;
    private String msg;

    AccountStateType(String code, String msg) {
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

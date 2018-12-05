package com.kaihei.esportingplus.payment.api.enums;

/**
 * 暴鸡币退款订单类型
 *
 * @author: xusisi
 **/
public enum GCoinRefundStateType {

    /***
     *000 退款失败
     */
    FAILED("000", "退款失败"),

    /***
     *001 退款中
     */
    CREATE("001", "退款中"),

    /**
     * 002 已退款
     */
    SUCCESS("002", "已退款");

    private String code;

    private String msg;

    GCoinRefundStateType(String code, String msg) {
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

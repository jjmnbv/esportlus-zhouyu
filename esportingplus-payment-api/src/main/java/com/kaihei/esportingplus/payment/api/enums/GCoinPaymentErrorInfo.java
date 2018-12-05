package com.kaihei.esportingplus.payment.api.enums;

/**
 * @program: esportingplus
 * @description: 暴鸡币支付订单类型
 * @author: xusisi
 * @create: 2018-09-23 16:23
 **/
public enum GCoinPaymentErrorInfo {


    /**
     * 000超时未支付
     */
    ORDER_TIMEOUT("000", "超时未支付"),

    /**
     * 001暴鸡币不足
     */
    BALANCE_NOT_ENOUGH("001", "暴鸡币不足"),

    /**
     * 002账户被冻结
     */

    ACCOUNT_IS_FROZEN("002", "账户被冻结");

    private String code;
    private String msg;

    GCoinPaymentErrorInfo(String code, String msg) {
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

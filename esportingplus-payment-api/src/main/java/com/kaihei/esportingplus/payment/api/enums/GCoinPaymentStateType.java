package com.kaihei.esportingplus.payment.api.enums;

/**
 * @program: esportingplus
 * @description: 暴鸡币支付订单类型
 * @author: xusisi
 * @create: 2018-09-23 16:23
 **/
public enum GCoinPaymentStateType {


    /***
     *000 支付失败
     */
    PAYMENT_FAILED("000", "支付失败，超时未支付"),
    /***
     * 001 待支付
     */
    WAIT_FOR_PAYMENT("001", "待支付"),
    /***
     * 002 支付成功，业务订单待结算
     */
    WAIT_FOR_SETTLE("002", "支付成功，业务订单待结算"),
    /***
     * 003 支付成功，业务订单已完成
     */
    BUSINESS_FINISH("003", "支付成功，业务订单已完成"),
    /***
     * 004 支付成功，退款中
     */
    REFUNDING("004", "支付成功，退款中"),
    /***
     * 005 已退款
     */
    REFUNDED("005", "已退款"),

    /***
     * 006 已结清，不可再退款
     */
    NON_REFUNDABLE("006", "已结清，不可再退款");




    private String code;

    private String msg;

    GCoinPaymentStateType(String code, String msg) {
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

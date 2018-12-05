package com.kaihei.esportingplus.payment.api.enums;

/**
 * @program: esportingplus-payment
 * @description: 提现订单状态 订单状态：001（提现交易创建）、002（提现交易超时关闭）、003（提现交易成功）
 * @author: xusisi
 * @create: 2018-08-18 11:29
 **/
public enum WithdrawStatusType {

    /***
     * 001（提现交易创建）
     */
    CREATE("001", "交易创建"),

    /***
     * 002（提现交易超时关闭）
     */
    CLOSE("002", "提现交易超时关闭"),
    /**
     * 003（提现交易成功）
     */
    SUCCESS("003", "提现交易成功"),

    /**
     * 004（提现失败）
     */
    FAIL("004", "提现失败");


    private String code;

    private String msg;

    WithdrawStatusType(String code, String msg) {
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

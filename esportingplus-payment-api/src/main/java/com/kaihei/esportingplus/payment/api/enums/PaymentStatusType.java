package com.kaihei.esportingplus.payment.api.enums;
/**
 * @program: esportingplus-payment
 * @description: 充值订单状态
 *  001（交易创建，等待买家付款）、002（未付款交易超时关闭，或支付完成后全额退款）、003（交易支付成功）、004（交易结束，不可退款）
 * @author: xusisi
 * @create: 2018-08-18 11:29
 **/
public enum PaymentStatusType {

    /***
     *001（交易创建，等待买家付款）
     */
    CREATE("001","交易创建，等待买家付款"),

    /***
     * 002（未付款交易超时关闭，或支付完成后全额退款）
     */
    CLOSE("002","未付款交易超时关闭，或支付完成后全额退款"),
    /**
     * 003（交易支付成功）
     */
    SUCCESS("003","交易支付成功"),
    /***
     * 004（交易结束，不可退款）
     */
    FINISH("004","交易结束，不可退款");

    private String code;

    private String msg;

    PaymentStatusType(String code, String msg) {
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

package com.kaihei.esportingplus.payment.api.enums;

/***
 * 支付宝支付订单状态
 */
public enum AlipayPayStateEnum {

    /***
     * 交易创建，等待买家付款
     */
    WAIT_BUYER_PAY("WAIT_BUYER_PAY", "交易创建，等待买家付款"),

    /***
     * 未付款交易超时关闭，或支付完成后全额退款
     */
    TRADE_CLOSED("TRADE_CLOSED", "未付款交易超时关闭，或支付完成后全额退款"),
    /***
     * 交易支付成功
     */
    TRADE_SUCCESS("TRADE_SUCCESS", "交易支付成功"),

    /***
     * 交易结束，不可退款
     */
    TRADE_FINISHED("TRADE_FINISHED", "交易结束，不可退款");

    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    AlipayPayStateEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

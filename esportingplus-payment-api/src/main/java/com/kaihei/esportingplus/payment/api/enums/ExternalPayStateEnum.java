package com.kaihei.esportingplus.payment.api.enums;

public enum ExternalPayStateEnum {

    /***
     * 订单待付款
     */
    UNPAIED("UNPAIED", "订单待付款"),

    /***
     * 订单关闭中（系统主动关闭）
     */
    CLOSING("CLOSING", "订单关闭中（系统主动关闭、全额退款）"),

    /***
     * 订单已关闭（系统主动关闭、全额退款）
     */
    CLOSED("CLOSED", "订单已关闭（系统主动关闭、全额退款）"),

    /***
     * 订单已付款
     */
    SUCCESS("SUCCESS", "订单已付款"),

    /***
     * 交易结束，不可退款
     */
    CLOSED_NO_REFUND("CLOSED_NO_REFUND", "交易结束，不可退款"),
    /***
     * 订单撤销中（用户主动）
     */
    CANCELING("CANCELING", "订单撤销中（用户主动）"),

    /***
     * 订单已撤销（用户主动）
     */
    CANCEL("CANCEL", "订单已撤销（用户主动）");


    private String code;

    private String msg;

    ExternalPayStateEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

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
}

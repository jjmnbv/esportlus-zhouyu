package com.kaihei.esportingplus.payment.api.enums;

public enum ExternalRefundStateEnum {


    /**
     * REFUNDING退款中
     */
    REFUNDING("REFUNDING", "退款中"),

    /***
     * SUCCESS 退款成功
     */
    SUCCESS("SUCCESS", "退款成功"),

    /***
     * FAILED 退款失败
     */
    FAILED("FAILED", "退款失败"),

    /***
     * CANCEL 订单已撤销（用户主动）
     */
    CANCEL("CANCEL", "订单已撤销（用户主动）");


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

    ExternalRefundStateEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

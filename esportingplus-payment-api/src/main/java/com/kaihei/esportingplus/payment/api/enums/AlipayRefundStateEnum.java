package com.kaihei.esportingplus.payment.api.enums;

/**
 * @program: esportingplus
 * @description: 支付宝退款状态枚举
 * @author: xusisi
 * @create: 2018-10-31 20:45
 **/
public enum AlipayRefundStateEnum {

    /***
     * 退款处理中
     */
    REFUND_PROCESSING("REFUND_PROCESSING", "退款处理中"),
    /***
     * 退款处理成功
     */
    REFUND_SUCCESS("REFUND_SUCCESS", "退款处理成功"),
    /***
     *退款失败
     */
    REFUND_FAIL("REFUND_FAIL", "退款失败");

    private String code;
    private String msg;

    AlipayRefundStateEnum(String code, String msg) {
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

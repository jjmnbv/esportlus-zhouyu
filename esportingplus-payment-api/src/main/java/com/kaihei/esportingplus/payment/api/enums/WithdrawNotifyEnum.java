package com.kaihei.esportingplus.payment.api.enums;

/**
 * @program: esportingplus-payment
 * @description: 提现是否需要通知
 * @author: chenzhenjun
 * @create: 2018-09-25 17:29
 **/
public enum WithdrawNotifyEnum {

    /***
     * WX（微信）
     */
    NEED("1", "需要通知"),

    /***
     * ZFB（支付宝）
     */
    NOE_NEED("0", "不需要通知");

    private String code;

    private String msg;

    WithdrawNotifyEnum(String code, String msg) {
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

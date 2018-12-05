package com.kaihei.esportingplus.payment.api.enums;

/**
 * @program: esportingplus-payment
 * @description:
 * @author: chenzhenjun
 * @create: 2018-09-25 17:29
 **/
public enum WithdrawType {

    /***
     * WX（微信）
     */
    WITHDRAW("withdraw", "提现"),

    /***
     * ZFB（支付宝）
     */
    EXCHANGE("exchange", "兑换");

    private String code;

    private String msg;

    WithdrawType(String code, String msg) {
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

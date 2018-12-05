package com.kaihei.esportingplus.payment.api.enums;

/**
 * @program: esportingplus-payment
 * @description: python返回结果
 * @author: chenzhenjun
 * @create: 2018-09-25 17:49
 **/
public enum WithdrawResultType {

    /***
     * success
     */
    SUCCESS("success", "提现成功"),

    /**
     * fail
     */
    FAIL("fail", "提现失败");


    private String code;

    private String msg;

    WithdrawResultType(String code, String msg) {
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

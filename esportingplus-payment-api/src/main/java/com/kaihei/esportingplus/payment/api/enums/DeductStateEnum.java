package com.kaihei.esportingplus.payment.api.enums;

/**
 * @program: esportingplus
 * @description: 暴鸡币扣款状态码
 * @author: xusisi
 * @create: 2018-10-09 18:14
 **/
public enum DeductStateEnum {

    /***
     * 0扣款成功
     */
    DEDUCT_SUCCESS("0", "扣款成功"),

    /***
     * 1扣款失败
     */
    DEDUCT_FAILED("1","扣款失败");

    private String code;

    private String msg;

    DeductStateEnum(String code, String msg) {
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

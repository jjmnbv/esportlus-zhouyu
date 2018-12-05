package com.kaihei.esportingplus.riskrating.api.enums;

/**
 * 调用数美接口返回码枚举类
 * @author chenzhenjun
 */
public enum ShumeiResultCodeEnum {

    SUCCESS(1100,"成功"),

    PARAMS_INVALID(1902,"参数不合法"),

    SERVICE_FAIL(1903,"服务失败"),

    BALANCE_LOW(9100,"余额不足"),

    PERMISSION_DENIED(9101,"无权限操作"),;

    /**
     * 事件编码
     */
    private int code;
    /**
     * 事件说明
     */
    private String msg;

    ShumeiResultCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}

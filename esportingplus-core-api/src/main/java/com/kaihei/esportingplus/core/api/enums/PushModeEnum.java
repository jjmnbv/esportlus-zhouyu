package com.kaihei.esportingplus.core.api.enums;

/**
 * @Author xusisi
 * @Description 推送方式枚举
 * @Date 2018/12/1 10:35
 **/
public enum PushModeEnum {
    /***
     * 1 ：立即推送
     */
    IMMEDIATELY(1, "立即推送");

    private int code;

    private String msg;

    PushModeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

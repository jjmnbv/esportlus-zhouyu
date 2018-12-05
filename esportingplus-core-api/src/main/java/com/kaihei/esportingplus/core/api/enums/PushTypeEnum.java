package com.kaihei.esportingplus.core.api.enums;

/**
 * @Author xusisi
 * @Description 推送类型枚举
 * @Date 2018/12/1 10:35
 **/
public enum PushTypeEnum {
    /***
     * 应用消息
     */
    APP_MESSAGE(1, "应用消息");

    private int code;
    private String msg;

    PushTypeEnum(int code, String msg) {
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

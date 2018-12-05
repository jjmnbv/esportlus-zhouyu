package com.kaihei.esportingplus.core.api.enums;

/**
 * @Author xusisi
 * @Description 平台枚举
 * @Date 2018/12/1 10:35
 **/
public enum PlatformEnum {
    /**
     * 1：ios
     */
    IOS(1, "ios"),

    /***
     * 2 : 安卓
     */
    ANDROID(2, "android");

    private int code;
    private String msg;

    PlatformEnum(int code, String msg) {
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

package com.kaihei.esportingplus.user.api.enums;

/**
 * @author linruihe
 * @description:手机系统类型
 * @date: 2018/10/29 15:18
 */
public enum PhoneSystemType {
    IOS(1,"iOS"),
    ANDROID(2,"Android");

    private Integer code;
    private String msg;

    PhoneSystemType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

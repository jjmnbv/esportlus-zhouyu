package com.kaihei.esportingplus.user.api.enums;

/**
 * @author linruihe
 * @description:登录类型
 * @date: 2018/10/29 15:18
 */
public enum LoginType {
    WX(1,"微信"),
    QQ(2,"QQ");

    private Integer code;
    private String msg;

    LoginType(Integer code, String msg) {
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

package com.kaihei.esportingplus.user.api.enums;

/**
 * @author linruihe
 * @description:数据类型枚举
 * @date: 2018/12/04 15:18
 */
public enum UserDataEnum {
    USER_ALL(0,"所有"),
    USER_FREE(1,"免费车队"),
    USER_PAY(2,"付费车队"),
    USER_TAXI(3,"滴滴单");

    private Integer code;
    private String msg;

    UserDataEnum(Integer code, String msg) {
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

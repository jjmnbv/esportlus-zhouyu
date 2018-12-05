package com.kaihei.esportingplus.user.api.enums;

/**
 * @author linruihe
 * @description:暴鸡状态类型
 * @date: 2018/10/29 15:18
 */
public enum BaojiStatusType {
    BAOJI_WATING(1,"待审核"),
    BAOJI_NORMAL(2,"正常"),
    BAOJI_FREEZE(3,"冻结"),
    //封禁暂时不需要考虑
    BAOJI_BANNED(4,"封禁");

    private Integer code;
    private String msg;

    BaojiStatusType(Integer code, String msg) {
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

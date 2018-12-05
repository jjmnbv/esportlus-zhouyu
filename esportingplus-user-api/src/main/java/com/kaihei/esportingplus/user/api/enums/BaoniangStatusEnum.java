package com.kaihei.esportingplus.user.api.enums;

/**
 * @author linruihe
 * @description:暴鸡状态类型
 * @date: 2018/10/29 15:18
 */
public enum BaoniangStatusEnum {
    BN_WATING(1,"待审核"),
    BN_NORMAL(2,"正常"),
    BN_FREEZE(3,"冻结"),
    BN_BANNED(4,"封禁");

    private Integer code;
    private String msg;

    BaoniangStatusEnum(Integer code, String msg) {
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

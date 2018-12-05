package com.kaihei.esportingplus.user.api.enums;

/**
 * @author linruihe
 * @description:能力类型
 * @date: 2018/10/29 15:18
 */
public enum PowersTypeType {
    POWER_WZRY_ONE(1, "国一"),
    POWER_WZRY_RY(2, "荣耀"),
    POWER_WZRY_WZ(3, "王者"),
    POWER_WZRY_XY(4, "星耀"),
    POWER_LOL_DS(5, "大师"),
    POWER_LOL_WZ(6, "王者"),
    POWER_LOL_ZS(7, "钻石"),
    POWER_JDQS_DS(8, "大神");

    private Integer code;
    private String msg;

    PowersTypeType(Integer code, String msg) {
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

package com.kaihei.esportingplus.marketing.api.enums;

/**
 * 车队角色类型枚举
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/10 15:44
 */
public enum TeamRoleType {

    /**
     * 老板
     */
    BOOS("0", "老板"),

    /**
     * 暴鸡
     */
    BAOJI("1", "暴鸡"),

    /**
     * 暴娘
     */
    BAONIANG("2", "暴娘");

    private String code;
    private String msg;

    TeamRoleType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

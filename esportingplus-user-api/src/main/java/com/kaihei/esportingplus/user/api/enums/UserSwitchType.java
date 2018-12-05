package com.kaihei.esportingplus.user.api.enums;

/**
 * @author zl.zhao
 * @description: 兑换暴击值身份配置
 * @date: 2018/10/23 14:15
 */
public enum UserSwitchType {
    /**
     * 暴鸡
     */
    BAOJI("baoji_switch", "暴鸡"),

    /**
     * 暴娘
     */
    BAONIANG("baoniang_switch", "暴娘"),

    /**
     * 普通用户
     */
    ORDINARY_USER("user_switch", "普通用户"),

    /**
     * 工作室暴鸡
     */
    GZSBAOJI("gzsbaoji_switch", "工作室暴鸡"),

    /**
     * 工作室暴娘
     */
    GZSBAONIANG("gzsbaoniang_switch", "工作室暴娘");

    private String code;
    private String msg;

    UserSwitchType(String code, String msg) {
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

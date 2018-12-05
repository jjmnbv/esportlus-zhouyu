package com.kaihei.esportingplus.user.api.enums;

/**
 * @author linruihe
 * @description:王者荣耀角色
 * @date: 2018/10/29 15:18
 */
public enum WzryPositionsType {
    TANK(1,"坦克"),
    WARRIOR(2,"战士"),
    ASSASSIN(3,"刺客"),
    MAGE(4,"法师"),
    SHOOTER(5,"射手"),
    SUPPORT(6,"辅助");

    private Integer code;
    private String msg;

    WzryPositionsType(Integer code, String msg) {
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

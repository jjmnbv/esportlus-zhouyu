package com.kaihei.esportingplus.user.api.enums;

/**
 * @author linruihe
 * @description:英雄联盟位置
 * @date: 2018/10/29 15:18
 */
public enum LolPositionsType {
    TOPLINE(1,"上单"),
    JUNGLE(2,"打野"),
    MIDLINE(3,"中单"),
    ADC(4,"ADC"),
    SUPPORT(5,"辅助");

    private Integer code;
    private String msg;

    LolPositionsType(Integer code, String msg) {
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

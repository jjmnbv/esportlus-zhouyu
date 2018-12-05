package com.kaihei.esportingplus.common.enums;

/**
 *@Description: DNF 副本位置枚举
 *
 *@author  Orochi-Yzh
 *@dateTime  2018/8/13 20:56
*/
public enum RaidLocationEnum {

    /** 打手 */
    DPS(1, "打手"),
    /** 辅助 */
    ASSIST(2, "辅助");

    private int code;
    private String desc;

    RaidLocationEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

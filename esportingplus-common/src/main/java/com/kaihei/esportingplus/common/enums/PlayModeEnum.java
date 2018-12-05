package com.kaihei.esportingplus.common.enums;

/**
 * 玩法模式枚举
 *
 * @author liangyi
 */
public enum PlayModeEnum {

    /** 上分 */
    GET_POINTS(0, "上分"),

    /** 陪玩 */
    ACCOMPANY_PLAY(1, "陪玩"),

    UNKNOWN(-1, "并没有这种类型,不存在的");

    private int code;
    private String desc;

    PlayModeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static PlayModeEnum getByCode(int code) {
        PlayModeEnum[] values = PlayModeEnum.values();
        for (PlayModeEnum playModeEnum : values) {
            if (playModeEnum.getCode() == code) {
                return playModeEnum;
            }
        }
        return UNKNOWN;
    }
}

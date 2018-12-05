package com.kaihei.esportingplus.gamingteam.api.enums;

/**
 * 车队状态枚举
 *
 * @author liangyi
 */
public enum TeamStatusEnum {

    /** 未知 ? */
    UNKNOWN(-1, "未知"),

    /**
     * 准备中
     */
    PREPARING(0, "准备中"),

    /**
     * 已发车(进行中)
     */
    RUNNING(1, "进行中"),

    /**
     * 已解散
     */
    DISMISSED(2, "已解散"),

    /**
     * 已完成
     */
    COMPLETED(3, "已完成");

    private int code;
    private String msg;

    TeamStatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static TeamStatusEnum convert(int code) {
        TeamStatusEnum teamStatusEnum = UNKNOWN;
        TeamStatusEnum[] values = TeamStatusEnum.values();
        for (TeamStatusEnum statusEnum : values) {
            if (statusEnum.getCode() == code) {
                teamStatusEnum = statusEnum;
                break;
            }
        }
        return teamStatusEnum;
    }

    public static TeamStatusEnum of(int code) {
        return convert(code);
    }
}

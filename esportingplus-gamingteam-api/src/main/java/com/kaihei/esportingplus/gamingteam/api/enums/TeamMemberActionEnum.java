package com.kaihei.esportingplus.gamingteam.api.enums;

/**
 * 车队队员在车队的行为枚举
 *
 * @author liangyi
 */
public enum TeamMemberActionEnum {

    /** 未知 ? */
    UNKNOWN(-1, "未知"),

    CREATE(1, "队长创建车队"),

    JOIN(2, "队员加入车队"),

    START(3, "队长立即开车"),

    DISMISS(4, "队长解散车队"),

    END(5, "队长正常结束车队"),

    QUIT(6, "队员退出车队"),

    KICK_OUT(7, "队员被踢出车队"),

    CURRENT_INFO(8, "队员获取车队实时信息"),

    RPG_CONFIRM_JOIN(9, "队员确认入团(只针对RPG车队)"),

    PVP_PREPARE(10, "队员准备(只针对PVP车队)"),

    PVP_CANCEL_PREPARE(11, "队员取消准备(只针对PVP车队)");

    private int code;
    private String msg;

    TeamMemberActionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static TeamMemberActionEnum getByCode(int code) {
        TeamMemberActionEnum teamStatusEnum = UNKNOWN;
        TeamMemberActionEnum[] values = TeamMemberActionEnum.values();
        for (TeamMemberActionEnum statusEnum : values) {
            if (statusEnum.getCode() == code) {
                teamStatusEnum = statusEnum;
                break;
            }
        }
        return teamStatusEnum;
    }
}

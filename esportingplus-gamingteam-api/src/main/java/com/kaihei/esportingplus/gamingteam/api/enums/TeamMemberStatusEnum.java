package com.kaihei.esportingplus.gamingteam.api.enums;

/**
 * 用户在车队中的状态枚举
 * 根据不同的身份有不同的状态:
 *      暴鸡: 准备入团->已入团
 *      老板: 待支付->准备入团(已支付)->已入团
 * @author liangyi
 */
public enum TeamMemberStatusEnum {

    /** 待支付 */
    WAIT_FOR_PAY(0, "待支付"),

    /** 准备入团 */
    PREPARE_JOIN_TEAM(1, "准备入团"),

    /** 已入团 */
    JOINED_TEAM(2, "已入团");

    private int code;
    private String msg;

    TeamMemberStatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static TeamMemberStatusEnum of(int code) {
        TeamMemberStatusEnum[] teamMemberStatusEnums = TeamMemberStatusEnum.values();
        for (TeamMemberStatusEnum teamMemberStatusEnum : teamMemberStatusEnums) {
            if (teamMemberStatusEnum.getCode() == code) {
                return teamMemberStatusEnum;
            }
        }
        return null;
    }
}

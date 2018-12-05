package com.kaihei.esportingplus.gamingteam.api.enums;

import com.kaihei.esportingplus.common.tools.CollectionUtils;

public enum PVPTeamMemberStatusEnum {
    /** 待准备 */
    WAIT_READY(0, "待准备"),

    /** 已准备 */
    PREPARE_READY(1, "已准备"),

    /** 已支付 */
    PAID(2, "已支付"),
    /** 开车后退出 */
    TEAM_START_QUIT(3, "开车后退出");

    private int code;
    private String msg;

    PVPTeamMemberStatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static PVPTeamMemberStatusEnum of(int code) {
        return CollectionUtils.find(PVPTeamMemberStatusEnum.values(), it -> it.getCode() == code)
                .orElse(null);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

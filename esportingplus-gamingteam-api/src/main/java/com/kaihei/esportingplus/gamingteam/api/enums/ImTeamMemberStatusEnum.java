package com.kaihei.esportingplus.gamingteam.api.enums;

import com.kaihei.esportingplus.common.constant.ImKey;

public enum ImTeamMemberStatusEnum {
    READY(0, "准备", ImKey.TEAM_MEMBER_READY), CANCEL_READY(1, "取消准备",
            ImKey.TEAM_MEMBER_CANCEL_READY), Another_order(10, "再来一单",
            ImKey.TEAM_MEMBER_ANOTHER_ORDER);
    private Integer code;
    private String desc;
    private String imKey;

    ImTeamMemberStatusEnum(Integer code, String desc, String imKey) {
        this.code = code;
        this.desc = desc;
        this.imKey = imKey;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getImKey() {
        return imKey;
    }

    public static ImTeamMemberStatusEnum getByCode(Integer code) {
        for (ImTeamMemberStatusEnum result : values()) {
            if (result.getCode().equals(code)) {
                return result;
            }
        }
        return null;
    }
}

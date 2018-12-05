package com.kaihei.esportingplus.gamingteam.data.manager.core.enums;

import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamMemberPVP;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamMemberPVPFree;
import java.util.Arrays;

/**
 * 根据数据库的teamType，在反序列化数据时，反序列化成不同的实体类
 */
public enum TeamMemberEnum {
    /**
     * 未知
     */
    UNKNOW(-1, TeamMember.class),

    /**
     * 免费车队
     */
    TEAM_MEMBER_PVP_FREE(0, TeamMemberPVPFree.class),
    /**
     * 付费车队
     */
    TEAM_MEMBER_PVP(1, TeamMemberPVP.class),
    ;
    private Integer teamType;

    private Class<? extends TeamMember> teamMemberType;

    TeamMemberEnum(Integer teamType,
            Class<? extends TeamMember> teamMemberType) {
        this.teamType = teamType;
        this.teamMemberType = teamMemberType;
    }

    public static Class<? extends TeamMember> of(Integer teamType) {
        return Arrays.stream(TeamMemberEnum.values())
                .filter(it -> it.getTeamType().equals(teamType))
                .findFirst().orElse(UNKNOW).getTeamMemberType();
    }

    public Integer getTeamType() {
        return teamType;
    }

    public Class<? extends TeamMember> getTeamMemberType() {
        return teamMemberType;
    }
}

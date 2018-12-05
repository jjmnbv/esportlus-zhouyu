package com.kaihei.esportingplus.gamingteam.data.manager.core.enums;

import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGamePVP;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGamePVPFree;
import java.util.Arrays;

/**
 * 根据数据库teamType在反序列化数据时 反序列化成不同的实体类
 */
public enum TeamGameEnum {
    /**
     * 未知
     */
    UNKNOW(-1, TeamGame.class),
    /**
     * 免费车队
     */
    TEAM_GAME_PVP_FREE(0, TeamGamePVPFree.class),
    /**
     * 付费车队
     */
    TEAM_GAME_PVP(1, TeamGamePVP.class),
    ;
    private Integer teamType;
    private Class<? extends TeamGame> teamGameType;


    TeamGameEnum(Integer teamType,
            Class<? extends TeamGame> teamGameType) {
        this.teamType = teamType;
        this.teamGameType = teamGameType;
    }

    public static Class<? extends TeamGame> of(Integer teamType) {
        return Arrays.stream(TeamGameEnum.values())
                .filter(it -> it.getTeamType().equals(teamType))
                .findFirst().orElse(UNKNOW).getTeamGameType();
    }

    public Integer getTeamType() {
        return teamType;
    }

    public Class<? extends TeamGame> getTeamGameType() {
        return teamGameType;
    }
}

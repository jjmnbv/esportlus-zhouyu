package com.kaihei.esportingplus.marketing.api.event;

import java.util.List;
import javax.validation.constraints.NotNull;

/**
 * TODO 功能描述
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/10 18:43
 */
public class TeamFreeEvent extends UserEvent {

    private static final long serialVersionUID = 2117925074579152317L;
    /**
     * 车队标识符
     */
    @NotNull(message = "车队标识符不能为空")
    private String slug;

    /**
     * 游戏类型（即车队类型）
     */
    @NotNull(message = "游戏类型不能为空")
    private String gameType;

    /**
     * 游戏大区
     */
    @NotNull(message = "游戏大区不能为空")
    private String gameZone;

    /**
     * 游戏结果, 0: 胜利 1: 失败 2: 未打
     */
    @NotNull(message = "游戏结果不能为空")
    private List<Integer> gameResult;

    /**
     * 车队成员列表
     */
    @NotNull(message = "车队成员列表不能为空")
    private List<TeamMember> members;

    /**
     * 车队成员列表
     */
    @NotNull(message = "车队类型不能为空")
    private String freeTeamType;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getGameZone() {
        return gameZone;
    }

    public void setGameZone(String gameZone) {
        this.gameZone = gameZone;
    }

    public List<Integer> getGameResult() {
        return gameResult;
    }

    public void setGameResult(List<Integer> gameResult) {
        this.gameResult = gameResult;
    }

    public List<TeamMember> getMembers() {
        return members;
    }

    public void setMembers(List<TeamMember> members) {
        this.members = members;
    }

    public String getFreeTeamType() {
        return freeTeamType;
    }

    public void setFreeTeamType(String freeTeamType) {
        this.freeTeamType = freeTeamType;
    }
}

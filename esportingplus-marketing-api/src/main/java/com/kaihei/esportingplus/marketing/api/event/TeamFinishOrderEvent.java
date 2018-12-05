package com.kaihei.esportingplus.marketing.api.event;

import java.util.List;

/**
 * @Auther: chen.junyong
 * @Date: 2018-12-03 11:17
 * @Description:
 */
public class TeamFinishOrderEvent extends UserEvent {

    private static final long serialVersionUID = 3292968574381652743L;

    /**
     * 车队序列号
     */
    private String teamSequence;
    //车队状态，0:未开车，1:已开车, 2:已结束
    private Integer teamStatus;
    /**
     * 游戏结果枚举Code
     */
    private Integer gameResultCode;

    private List<TeamMember> teamMemberVOS;

    public String getTeamSequence() {
        return teamSequence;
    }

    public void setTeamSequence(String teamSequence) {
        this.teamSequence = teamSequence;
    }

    public Integer getTeamStatus() {
        return teamStatus;
    }

    public void setTeamStatus(Integer teamStatus) {
        this.teamStatus = teamStatus;
    }

    public Integer getGameResultCode() {
        return gameResultCode;
    }

    public void setGameResultCode(Integer gameResultCode) {
        this.gameResultCode = gameResultCode;
    }

    public List<TeamMember> getTeamMemberVOS() {
        return teamMemberVOS;
    }

    public void setTeamMemberVOS(
            List<TeamMember> teamMemberVOS) {
        this.teamMemberVOS = teamMemberVOS;
    }
}

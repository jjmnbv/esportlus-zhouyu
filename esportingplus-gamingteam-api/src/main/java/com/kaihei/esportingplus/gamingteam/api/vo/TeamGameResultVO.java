package com.kaihei.esportingplus.gamingteam.api.vo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 车队比赛结果
 * @author liangyi
 */
@Builder
@AllArgsConstructor
public class TeamGameResultVO implements Serializable{

    private static final long serialVersionUID = -6645479506350622525L;

    /**
     * 车队序列号
     */
    private String teamSequence;

    /**
     * 车队标题
     */
    private String teamTitle;

    /**
     * 比赛结果序号, 第几局
     */
    private Integer resultSequence;

    /**
     * 车队比赛结果 code {@link com.kaihei.esportingplus.common.enums.GameResultEnum}
     */
    private Integer gameResultCode;

    /**
     * 车队比赛结果 描述 {@link com.kaihei.esportingplus.common.enums.GameResultEnum}
     */
    private String gameResultDesc;

    public TeamGameResultVO() {
    }

    public String getTeamSequence() {
        return teamSequence;
    }

    public void setTeamSequence(String teamSequence) {
        this.teamSequence = teamSequence;
    }

    public String getTeamTitle() {
        return teamTitle;
    }

    public void setTeamTitle(String teamTitle) {
        this.teamTitle = teamTitle;
    }

    public Integer getResultSequence() {
        return resultSequence;
    }

    public void setResultSequence(Integer resultSequence) {
        this.resultSequence = resultSequence;
    }

    public Integer getGameResultCode() {
        return gameResultCode;
    }

    public void setGameResultCode(Integer gameResultCode) {
        this.gameResultCode = gameResultCode;
    }

    public String getGameResultDesc() {
        return gameResultDesc;
    }

    public void setGameResultDesc(String gameResultDesc) {
        this.gameResultDesc = gameResultDesc;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

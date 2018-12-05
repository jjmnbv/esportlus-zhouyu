package com.kaihei.esportingplus.gamingteam.api.vo;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class GameTeamTotal implements Serializable {

    /**
     * 游戏类型，指游戏code
     */
    private Integer gameType;
    /**
     * 符合条件的数量
     */
    private Long premadeNum;

    public Integer getGameType() {
        return gameType;
    }

    public void setGameType(Integer gameType) {
        this.gameType = gameType;
    }

    public Long getPremadeNum() {
        return premadeNum;
    }

    public void setPremadeNum(Long premadeNum) {
        this.premadeNum = premadeNum;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

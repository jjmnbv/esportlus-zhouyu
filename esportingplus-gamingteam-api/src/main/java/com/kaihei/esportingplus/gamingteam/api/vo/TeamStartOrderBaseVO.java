package com.kaihei.esportingplus.gamingteam.api.vo;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 立即开车订单基础 VO
 * @author zhangfang
 */
public class TeamStartOrderBaseVO implements Serializable {

    private static final long serialVersionUID = -4113040413905665952L;
    
    /** 车队序列号 */
    private String sequence;

    /** 游戏 code */
    private Integer gameCode;

    /** 游戏 code */
    private String gameName;

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public Integer getGameCode() {
        return gameCode;
    }

    public void setGameCode(Integer gameCode) {
        this.gameCode = gameCode;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

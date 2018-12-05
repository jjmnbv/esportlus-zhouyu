package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 游戏位置数
 * @author liangyi
 */
public class GamePositionVO implements Serializable {

    private static final long serialVersionUID = 3518141135109138492L;

    /**
     * 游戏 id
     */
    private Integer gameId;

    /**
     * 最大位置数
     */
    private Integer maxPositionCount;

    public GamePositionVO() {
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Integer getMaxPositionCount() {
        return maxPositionCount;
    }

    public void setMaxPositionCount(Integer maxPositionCount) {
        this.maxPositionCount = maxPositionCount;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

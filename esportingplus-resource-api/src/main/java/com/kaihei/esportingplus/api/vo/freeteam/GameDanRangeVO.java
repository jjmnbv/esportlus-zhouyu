package com.kaihei.esportingplus.api.vo.freeteam;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 游戏段位范围
 *
 * @author liangyi
 */
@AllArgsConstructor
@Builder
public class GameDanRangeVO implements Serializable {

    private static final long serialVersionUID = -1576840754514881655L;

    /**
     * 段位下限 id
     */
    private Integer lowerDanId;

    /**
     * 段位上限 id
     */
    private Integer upperDanId;

    public GameDanRangeVO() {
    }

    public Integer getLowerDanId() {
        return lowerDanId;
    }

    public void setLowerDanId(Integer lowerDanId) {
        this.lowerDanId = lowerDanId;
    }

    public Integer getUpperDanId() {
        return upperDanId;
    }

    public void setUpperDanId(Integer upperDanId) {
        this.upperDanId = upperDanId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

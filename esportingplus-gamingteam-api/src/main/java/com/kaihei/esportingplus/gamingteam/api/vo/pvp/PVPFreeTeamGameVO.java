package com.kaihei.esportingplus.gamingteam.api.vo.pvp;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 免费车队所属的游戏信息
 * @author liangyi
 */
@Builder
@AllArgsConstructor
public class PVPFreeTeamGameVO implements Serializable {

    private static final long serialVersionUID = 3127878261593464721L;

    /**
     * 免费车队类型名称
     */
    private String freeTeamTypeName;

    /**
     * 大区名称
     */
    private String gameZoneName;

    /**
     * 段位下限名称
     */
    private String lowerDanName;

    /**
     * 段位上限名称
     */
    private String upperDanName;

    public PVPFreeTeamGameVO() {
    }

    public String getFreeTeamTypeName() {
        return freeTeamTypeName;
    }

    public void setFreeTeamTypeName(String freeTeamTypeName) {
        this.freeTeamTypeName = freeTeamTypeName;
    }

    public String getGameZoneName() {
        return gameZoneName;
    }

    public void setGameZoneName(String gameZoneName) {
        this.gameZoneName = gameZoneName;
    }

    public String getLowerDanName() {
        return lowerDanName;
    }

    public void setLowerDanName(String lowerDanName) {
        this.lowerDanName = lowerDanName;
    }

    public String getUpperDanName() {
        return upperDanName;
    }

    public void setUpperDanName(String upperDanName) {
        this.upperDanName = upperDanName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

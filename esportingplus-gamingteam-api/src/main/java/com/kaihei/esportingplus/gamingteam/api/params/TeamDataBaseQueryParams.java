package com.kaihei.esportingplus.gamingteam.api.params;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TeamDataBaseQueryParams {

    /**
     * 游戏代码，必填
     */
    private Integer gameCode;
    /**
     * 副本Code
     */
    private Integer raidCode;
    /**
     * 跨区代码
     */
    private Integer zoneAcrossCode;

    public TeamDataBaseQueryParams(Integer gameCode, Integer raidCode, Integer zoneAcrossCode) {
        this.gameCode = gameCode;
        this.raidCode = raidCode;
        this.zoneAcrossCode = zoneAcrossCode;
    }

    public TeamDataBaseQueryParams() {
    }

    public Integer getGameCode() {
        return gameCode;
    }

    public void setGameCode(Integer gameCode) {
        this.gameCode = gameCode;
    }

    public Integer getRaidCode() {
        return raidCode;
    }

    public void setRaidCode(Integer raidCode) {
        this.raidCode = raidCode;
    }

    public Integer getZoneAcrossCode() {
        return zoneAcrossCode;
    }

    public void setZoneAcrossCode(Integer zoneAcrossCode) {
        this.zoneAcrossCode = zoneAcrossCode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

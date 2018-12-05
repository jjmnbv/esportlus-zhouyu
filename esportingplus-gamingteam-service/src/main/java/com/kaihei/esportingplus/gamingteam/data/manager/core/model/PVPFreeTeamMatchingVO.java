package com.kaihei.esportingplus.gamingteam.data.manager.core.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 免费车队车队的匹配信息
 *
 * @author liangyi
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PVPFreeTeamMatchingVO implements Serializable{

    private static final long serialVersionUID = 9131553643117117326L;

    /**
     * 融云群组Id
     */
    private String groupId;

    /**
     * 车队sequence
     */
    private String teamSequence;

    /**
     * 空位数
     */
    private Integer freeMember;

    /**
     * 车队创建时间
     */
    private LocalDateTime teamCreateTime;

    /**
     * 游戏段位上限
     */
    private Integer gameDanTopLimit;

    /**
     * 游戏段位下限
     */
    private Integer gameDanLowerLimit;

    public String getTeamSequence() {
        return teamSequence;
    }

    public void setTeamSequence(String teamSequence) {
        this.teamSequence = teamSequence;
    }

    public Integer getFreeMember() {
        return freeMember;
    }

    public void setFreeMember(Integer freeMember) {
        this.freeMember = freeMember;
    }

    public LocalDateTime getTeamCreateTime() {
        return teamCreateTime;
    }

    public void setTeamCreateTime(LocalDateTime teamCreateTime) {
        this.teamCreateTime = teamCreateTime;
    }

    public Integer getGameDanTopLimit() {
        return gameDanTopLimit;
    }

    public void setGameDanTopLimit(Integer gameDanTopLimit) {
        this.gameDanTopLimit = gameDanTopLimit;
    }

    public Integer getGameDanLowerLimit() {
        return gameDanLowerLimit;
    }

    public void setGameDanLowerLimit(Integer gameDanLowerLimit) {
        this.gameDanLowerLimit = gameDanLowerLimit;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

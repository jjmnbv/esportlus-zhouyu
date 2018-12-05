package com.kaihei.esportingplus.gamingteam.data.manager.core.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 免费车队老板匹配的redis key 路由信息
 *
 * @author liangyi
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PVPFreeTeamMatchingRouteVO implements Serializable{

    private static final long serialVersionUID = 2821783829473096909L;
    /**
     * 结算服务类型
     */
    private Integer settlementType;

    /**
     * 车队类型 id
     */
    private Integer teamTypeId;

    /**
     * 游戏大区 id
     */
    private Integer gameZoneId;

    public Integer getSettlementType() {
        return settlementType;
    }

    public void setSettlementType(Integer settlementType) {
        this.settlementType = settlementType;
    }

    public Integer getGameZoneId() {
        return gameZoneId;
    }

    public void setGameZoneId(Integer gameZoneId) {
        this.gameZoneId = gameZoneId;
    }

    public Integer getTeamTypeId() {
        return teamTypeId;
    }

    public void setTeamTypeId(Integer teamTypeId) {
        this.teamTypeId = teamTypeId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

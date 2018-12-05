package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;
import java.util.List;

public class TeamOrderVo implements Serializable{

    /**
     * 游戏名称
     */
    private String gameName;
    /**
     *  副本名称
     */

    private String raidName;
    /**
     * 跨区名称
     */
    private String zoneAcrossName;
    /**
     * 比赛结果
     */
    private Integer gameResult;
    /**
     * 查询者自己的订单
     */
    private TeamMemberOrderVo ownerOrder;
    /**
     * 其它人订单
     */
    private List<TeamMemberOrderVo> otherOrders;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getRaidName() {
        return raidName;
    }

    public void setRaidName(String raidName) {
        this.raidName = raidName;
    }

    public String getZoneAcrossName() {
        return zoneAcrossName;
    }

    public void setZoneAcrossName(String zoneAcrossName) {
        this.zoneAcrossName = zoneAcrossName;
    }

    public Integer getGameResult() {
        return gameResult;
    }

    public void setGameResult(Integer gameResult) {
        this.gameResult = gameResult;
    }

    public TeamMemberOrderVo getOwnerOrder() {
        return ownerOrder;
    }

    public void setOwnerOrder(TeamMemberOrderVo ownerOrder) {
        this.ownerOrder = ownerOrder;
    }

    public List<TeamMemberOrderVo> getOtherOrders() {
        return otherOrders;
    }

    public void setOtherOrders(
            List<TeamMemberOrderVo> otherOrders) {
        this.otherOrders = otherOrders;
    }
}

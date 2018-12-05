package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 订单详情
 * @author zhangfang
 */
public class PVPTeamOrderVo implements Serializable{

    private static final long serialVersionUID = 7229551905575162805L;
    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * 大区名称
     */
    private String gameZoneName;

    /**
     * 玩法类型名称
     */
    private String playModeName;
    /**
     * 结算类型, 局; 小时
     */
    private String settlementTypeName;

    /**
     * 结算数量名称
     */
    private String settlementNumber;
    /**
     * 比赛结果
     */
    private PVPGameResult gameResult;
    /**
     * 查询者自己的订单
     */
    private PVPTeamMemberOrderVO ownerOrder;
    /**
     * 其它人订单
     */
    private List<PVPTeamMemberOrderVO> otherOrders;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }



    public String getGameZoneName() {
        return gameZoneName;
    }

    public void setGameZoneName(String gameZoneName) {
        this.gameZoneName = gameZoneName;
    }

    public String getPlayModeName() {
        return playModeName;
    }

    public void setPlayModeName(String playModeName) {
        this.playModeName = playModeName;
    }

    public String getSettlementTypeName() {
        return settlementTypeName;
    }

    public void setSettlementTypeName(String settlementTypeName) {
        this.settlementTypeName = settlementTypeName;
    }

    public String getSettlementNumber() {
        return settlementNumber;
    }

    public void setSettlementNumber(String settlementNumber) {
        this.settlementNumber = settlementNumber;
    }

    public PVPGameResult getGameResult() {
        return gameResult;
    }

    public void setGameResult(PVPGameResult gameResult) {
        this.gameResult = gameResult;
    }

    public TeamMemberOrderVo getOwnerOrder() {
        return ownerOrder;
    }

    public void setOwnerOrder(PVPTeamMemberOrderVO ownerOrder) {
        this.ownerOrder = ownerOrder;
    }

    public List<PVPTeamMemberOrderVO> getOtherOrders() {
        return otherOrders;
    }

    public void setOtherOrders(
            List<PVPTeamMemberOrderVO> otherOrders) {
        this.otherOrders = otherOrders;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

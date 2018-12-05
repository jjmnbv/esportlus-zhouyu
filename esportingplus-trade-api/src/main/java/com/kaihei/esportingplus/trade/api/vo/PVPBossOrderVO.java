package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *老板订单
 * @author zhangfang
 */
public class PVPBossOrderVO implements Serializable{

    private static final long serialVersionUID = -8689090337297556703L;
    /**
     * 订单状态，0：准备中(暴鸡队员）, 1：待付款（老板上车后的状态），2：已付款（老板付款后的状态），3：已取消，4：已完成
     */
    private Byte status;

    /**
     * 订单序列号
     */
    private String sequeue;

    /**
     * 订单创建时间
     */
    private Date gmtCreate;

    /**
     * 游戏名称
     */
    private String gameName;
    /**
     * 大区名称
     */
    private String gameZoneName;
    /**
     * 段位名称
     */
    private String gameDanName;
    /**
     * 玩法模式, 0:上分; 1:陪玩;
     */
    private Byte playMode;

    /**
     * 结算类型, 1:局; 2:小时
     */
    private Byte settlementType;
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
     * 实际支付金额 = 实际支付金额 - 退款金额
     */
    private Integer actualPaidAmount;

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getSequeue() {
        return sequeue;
    }

    public void setSequeue(String sequeue) {
        this.sequeue = sequeue;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

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

    public String getGameDanName() {
        return gameDanName;
    }

    public void setGameDanName(String gameDanName) {
        this.gameDanName = gameDanName;
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

    public Integer getActualPaidAmount() {
        return actualPaidAmount;
    }

    public void setActualPaidAmount(Integer actualPaidAmount) {
        this.actualPaidAmount = actualPaidAmount;
    }

    public Byte getPlayMode() {
        return playMode;
    }

    public void setPlayMode(Byte playMode) {
        this.playMode = playMode;
    }

    public Byte getSettlementType() {
        return settlementType;
    }

    public void setSettlementType(Byte settlementType) {
        this.settlementType = settlementType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

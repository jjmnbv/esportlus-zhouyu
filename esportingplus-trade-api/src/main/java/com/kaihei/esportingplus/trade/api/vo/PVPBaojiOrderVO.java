package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 暴鸡订单
 */
public class PVPBaojiOrderVO implements Serializable{

    private static final long serialVersionUID = 7050403878867150311L;

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
     * 订单收益金额
     */
    private Integer price;

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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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

    public String getSettlementNumber() {
        return settlementNumber;
    }

    public void setSettlementNumber(String settlementNumber) {
        this.settlementNumber = settlementNumber;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

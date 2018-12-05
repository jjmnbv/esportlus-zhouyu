package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author admin
 */
public class StudioOrderListVo implements Serializable {

    private static final long serialVersionUID = 8282465675570612655L;

    /**
     * 订单ID
     */
    private String id;

    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * 副本名称
     */
    private String raidName;

    /**
     * 跨区名称
     */
    private String zoneAcrossName;

    /**
     * 车队标题
     */
    private String teamTitle;

    /**
     * 车队序列号
     */
    private String teamSequeue;

    /**
     * 车队原价
     */
    private int teamOriginalPrice;

    /**
     * 实际获得收益
     */
    private int price;

    /**
     * 暴鸡昵称
     */
    private String userNickname;

    /**
     * 暴鸡UID
     */
    private String userUid;

    /**
     * 暴鸡牌号
     */
    private String userChickenId;

    /**
     * 订单状态
     */
    private int orderStatus;

    /**
     * 订单创建时间
     */
    private Date orderDateCreated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getTeamTitle() {
        return teamTitle;
    }

    public void setTeamTitle(String teamTitle) {
        this.teamTitle = teamTitle;
    }

    public String getTeamSequeue() {
        return teamSequeue;
    }

    public void setTeamSequeue(String teamSequeue) {
        this.teamSequeue = teamSequeue;
    }

    public int getTeamOriginalPrice() {
        return teamOriginalPrice;
    }

    public void setTeamOriginalPrice(int teamOriginalPrice) {
        this.teamOriginalPrice = teamOriginalPrice;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUserChickenId() {
        return userChickenId;
    }

    public void setUserChickenId(String userChickenId) {
        this.userChickenId = userChickenId;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getOrderDateCreated() {
        return orderDateCreated;
    }

    public void setOrderDateCreated(Date orderDateCreated) {
        this.orderDateCreated = orderDateCreated;
    }

    @Override
    public String toString() {
        return "StudioOrderListVo{" +
                "id='" + id + '\'' +
                ", gameName='" + gameName + '\'' +
                ", raidName='" + raidName + '\'' +
                ", zoneAcrossName='" + zoneAcrossName + '\'' +
                ", teamTitle='" + teamTitle + '\'' +
                ", teamSequeue='" + teamSequeue + '\'' +
                ", teamOriginalPrice=" + teamOriginalPrice +
                ", price=" + price +
                ", userNickname='" + userNickname + '\'' +
                ", userUid='" + userUid + '\'' +
                ", userChickenId='" + userChickenId + '\'' +
                ", orderStatus=" + orderStatus +
                ", orderDateCreated=" + orderDateCreated +
                '}';
    }
}


package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;
import java.util.Date;

public class StudioUserOrder implements Serializable{

    private static final long serialVersionUID = -2880307044428077342L;
    /**
     * 订单id
     */
    private Long id;
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
    private String teamSequence;
    /**
     *车队原价
     */
    private Integer teamOriginalPrice;
    /**
     * 暴鸡收益
     */
    private Integer price;
    /**
     * 用户昵称
     */
    private String userNickName;
    /**
     * 用户uid
     */
    private String userUid;
    /**
     * 用户暴鸡号
     */
    private String userChickenId;
    /**
     * 订单状态
     */
    private Integer orderStatus;
    /**
     * 订单状态描述
     */
    private String orderStatusDesc;
    /**
     * 订单创建时间
     */
    private Date orderDateCreate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getTeamSequence() {
        return teamSequence;
    }

    public void setTeamSequence(String teamSequence) {
        this.teamSequence = teamSequence;
    }

    public Integer getTeamOriginalPrice() {
        return teamOriginalPrice;
    }

    public void setTeamOriginalPrice(Integer teamOriginalPrice) {
        this.teamOriginalPrice = teamOriginalPrice;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
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

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusDesc() {
        return orderStatusDesc;
    }

    public void setOrderStatusDesc(String orderStatusDesc) {
        this.orderStatusDesc = orderStatusDesc;
    }

    public Date getOrderDateCreate() {
        return orderDateCreate;
    }

    public void setOrderDateCreate(Date orderDateCreate) {
        this.orderDateCreate = orderDateCreate;
    }
}

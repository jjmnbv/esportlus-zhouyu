package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;

/**
 * @author admin
 */
public class OrderItemTeamVo implements Serializable {

    private static final long serialVersionUID = -541527859600264395L;
    /**
     * 订单用户UID
     */
    private String uid;
    /**
     * 车队序列号
     */
    private String teamSequeue;
    /**
     * 暴鸡等级 100: 普通暴鸡 101: 优选暴鸡 102: 超级暴鸡 300: 暴娘
     */
    private Integer userBaojiLevel;
    /**
     * 队员身份，0：老板，1：暴鸡，2：暴娘，3：队长
     */
    private Integer userIdentity;
    /**
     * 游戏code
     */
    private Integer gameCode;

    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * 游戏角色ID
     */
    private Long gameRoleId;

    /**
     * 游戏角色名称
     */
    private String gameRoleName;

    /**
     * 副本code
     */
    private Integer raidCode;

    /**
     * 副本名称
     */
    private String raidName;

    /**
     * 副本位置code
     */
    private Integer raidLocationCode;

    /**
     * 副本位置名称
     */
    private String raidLocationName;

    /**
     * 跨区code
     */
    private Integer zoneAcrossCode;

    /**
     * 跨区名称
     */
    private String zoneAcrossName;

    /**
     * 单价，单位：分,  老板支付或暴鸡收益
     */
    private Integer price;

    public OrderItemTeamVo() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getUserBaojiLevel() {
        return userBaojiLevel;
    }

    public void setUserBaojiLevel(Integer userBaojiLevel) {
        this.userBaojiLevel = userBaojiLevel;
    }

    public Integer getGameCode() {
        return gameCode;
    }

    public void setGameCode(Integer gameCode) {
        this.gameCode = gameCode;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getTeamSequeue() {
        return teamSequeue;
    }

    public void setTeamSequeue(String teamSequeue) {
        this.teamSequeue = teamSequeue;
    }

    public Integer getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(Integer userIdentity) {
        this.userIdentity = userIdentity;
    }

    public Long getGameRoleId() {
        return gameRoleId;
    }

    public void setGameRoleId(Long gameRoleId) {
        this.gameRoleId = gameRoleId;
    }

    public String getGameRoleName() {
        return gameRoleName;
    }

    public void setGameRoleName(String gameRoleName) {
        this.gameRoleName = gameRoleName;
    }

    public Integer getRaidCode() {
        return raidCode;
    }

    public void setRaidCode(Integer raidCode) {
        this.raidCode = raidCode;
    }

    public String getRaidName() {
        return raidName;
    }

    public void setRaidName(String raidName) {
        this.raidName = raidName;
    }

    public Integer getRaidLocationCode() {
        return raidLocationCode;
    }

    public void setRaidLocationCode(Integer raidLocationCode) {
        this.raidLocationCode = raidLocationCode;
    }

    public String getRaidLocationName() {
        return raidLocationName;
    }

    public void setRaidLocationName(String raidLocationName) {
        this.raidLocationName = raidLocationName;
    }

    public Integer getZoneAcrossCode() {
        return zoneAcrossCode;
    }

    public void setZoneAcrossCode(Integer zoneAcrossCode) {
        this.zoneAcrossCode = zoneAcrossCode;
    }

    public String getZoneAcrossName() {
        return zoneAcrossName;
    }

    public void setZoneAcrossName(String zoneAcrossName) {
        this.zoneAcrossName = zoneAcrossName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}

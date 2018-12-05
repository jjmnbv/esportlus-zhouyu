package com.kaihei.esportingplus.gamingteam.api.vo;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 老板创建订单时需要的 VO 对象
 * @author liangyi
 */
public class BossInfoForOrderVO implements Serializable {

    private static final long serialVersionUID = 2588485884118612868L;

    /** 车队序列号 */
    private String sequence;

    /** 游戏 code */
    private Integer gameCode;

    /** 游戏 code */
    private String gameName;

    /** 副本 code */
    private Integer raidCode;

    /** 副本名称 */
    private String raidName;

    /** 车队跨区 code */
    private Integer zoneAcrossCode;

    /** 车队跨区名称 */
    private String zoneAcrossName;

    /** 攻坚队名称 */
    private String assaultName;

    /** 老板支付金额(即车队折扣价) */
    private Integer price;

    /**
     * 车队队员UID
     */
    private String uid;

    /**
     * 车队队员昵称
     */
    private String username;

    /**
     * 车队队员头像
     */
    private String avatar;

    /**
     * 车队队员游戏角色ID
     */
    private Long gameRoleId;

    /**
     * 车队队员游戏角色名称
     */
    private String gameRoleName;

    /**
     * 车队队员游戏角色的职业 code
     */
    private String careerCode;

    /**
     * 车队队员游戏角色的职业 name
     */
    private String careerName;

    /**
     * 车队队员状态(0: 待支付(只针对老板), 1: 准备入团, 2: 已入团)
     */
    private Integer status;

    public BossInfoForOrderVO() {
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
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

    public String getAssaultName() {
        return assaultName;
    }

    public void setAssaultName(String assaultName) {
        this.assaultName = assaultName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public String getCareerCode() {
        return careerCode;
    }

    public void setCareerCode(String careerCode) {
        this.careerCode = careerCode;
    }

    public String getCareerName() {
        return careerName;
    }

    public void setCareerName(String careerName) {
        this.careerName = careerName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

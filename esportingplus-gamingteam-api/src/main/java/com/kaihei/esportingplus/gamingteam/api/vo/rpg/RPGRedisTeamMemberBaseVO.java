package com.kaihei.esportingplus.gamingteam.api.vo.rpg;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 缓存在 redis 中的车队队员基础信息
 * @author liangyi
 */
public class RPGRedisTeamMemberBaseVO implements Serializable {

    private static final long serialVersionUID = -3358636763245944915L;

    /**
     * 车队队员UID
     */
    private String uid;

    /**
     * 车队队员昵称
     */
    private String username;

    /**
     * 暴鸡等级, 100: 普通暴鸡, 101: 优选暴鸡, 102: 超级暴鸡, 暴娘: 300
     */
    private Integer baojiLevel;

    /**
     * 车队队员身份，0：老板，1：暴鸡，2：暴娘，9：队长
     */
    private Integer userIdentity;

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
     * 副本位置 code
     */
    private Integer raidLocationCode;

    /**
     * 副本位置名称
     */
    private String raidLocationName;

    /** 角色小区 code */
    private Integer zoneSmallCode;

    /** 角色小区 name */
    private String zoneSmallName;

    /**
     * 上车时间
     */
    private Date joinTime;

    /**
     * 车队队员状态(0: 待支付(只针对老板), 1: 准备入团, 2: 已入团)
     */
    private Integer status;


    public RPGRedisTeamMemberBaseVO() {
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

    public Integer getBaojiLevel() {
        return baojiLevel;
    }

    public void setBaojiLevel(Integer baojiLevel) {
        this.baojiLevel = baojiLevel;
    }

    public Integer getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(Integer userIdentity) {
        this.userIdentity = userIdentity;
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

    public Integer getZoneSmallCode() {
        return zoneSmallCode;
    }

    public void setZoneSmallCode(Integer zoneSmallCode) {
        this.zoneSmallCode = zoneSmallCode;
    }

    public String getZoneSmallName() {
        return zoneSmallName;
    }

    public void setZoneSmallName(String zoneSmallName) {
        this.zoneSmallName = zoneSmallName;
    }

    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
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

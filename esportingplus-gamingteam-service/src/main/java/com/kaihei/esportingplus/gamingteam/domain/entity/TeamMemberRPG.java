package com.kaihei.esportingplus.gamingteam.domain.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "team_member_rpg")
public class TeamMemberRPG {
    /**
     * 车队队员表主键ID
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 所属车队ID
     */
    @Column(name = "team_id")
    private Long teamId;

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
    @Column(name = "baoji_level")
    private Integer baojiLevel;

    /**
     * 车队队员身份，0：老板，1：暴鸡，2：暴娘，9：队长
     */
    @Column(name = "user_identity")
    private Byte userIdentity;

    /**
     * 车队队员头像
     */
    private String avatar;

    /**
     * 上车时间
     */
    @Column(name = "join_time")
    private Date joinTime;

    /**
     * 车队队员游戏角色ID
     */
    @Column(name = "game_role_id")
    private Long gameRoleId;

    /**
     * 车队队员游戏角色名称
     */
    @Column(name = "game_role_name")
    private String gameRoleName;

    /**
     * 副本位置 code
     */
    @Column(name = "raid_location_code")
    private Integer raidLocationCode;

    /**
     * 副本位置名称
     */
    @Column(name = "raid_location_name")
    private String raidLocationName;

    /**
     * 车队队员状态(0: 待支付(只针对老板), 1: 待入团, 2: 已入团)
     */
    private Byte status;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    private Date gmtModified;

    public TeamMemberRPG(Long id, Long teamId, String uid, String username, Integer baojiLevel, Byte userIdentity, String avatar, Date joinTime, Long gameRoleId, String gameRoleName, Integer raidLocationCode, String raidLocationName, Byte status, Date gmtCreate, Date gmtModified) {
        this.id = id;
        this.teamId = teamId;
        this.uid = uid;
        this.username = username;
        this.baojiLevel = baojiLevel;
        this.userIdentity = userIdentity;
        this.avatar = avatar;
        this.joinTime = joinTime;
        this.gameRoleId = gameRoleId;
        this.gameRoleName = gameRoleName;
        this.raidLocationCode = raidLocationCode;
        this.raidLocationName = raidLocationName;
        this.status = status;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public TeamMemberRPG() {
        super();
    }

    /**
     * 获取车队队员表主键ID
     *
     * @return id - 车队队员表主键ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置车队队员表主键ID
     *
     * @param id 车队队员表主键ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取所属车队ID
     *
     * @return team_id - 所属车队ID
     */
    public Long getTeamId() {
        return teamId;
    }

    /**
     * 设置所属车队ID
     *
     * @param teamId 所属车队ID
     */
    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    /**
     * 获取车队队员UID
     *
     * @return uid - 车队队员UID
     */
    public String getUid() {
        return uid;
    }

    /**
     * 设置车队队员UID
     *
     * @param uid 车队队员UID
     */
    public void setUid(String uid) {
        this.uid = uid == null ? null : uid.trim();
    }

    /**
     * 获取车队队员昵称
     *
     * @return username - 车队队员昵称
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置车队队员昵称
     *
     * @param username 车队队员昵称
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * 获取暴鸡等级, 100: 普通暴鸡, 101: 优选暴鸡, 102: 超级暴鸡, 暴娘: 300
     *
     * @return baoji_level - 暴鸡等级, 100: 普通暴鸡, 101: 优选暴鸡, 102: 超级暴鸡, 暴娘: 300
     */
    public Integer getBaojiLevel() {
        return baojiLevel;
    }

    /**
     * 设置暴鸡等级, 100: 普通暴鸡, 101: 优选暴鸡, 102: 超级暴鸡, 暴娘: 300
     *
     * @param baojiLevel 暴鸡等级, 100: 普通暴鸡, 101: 优选暴鸡, 102: 超级暴鸡, 暴娘: 300
     */
    public void setBaojiLevel(Integer baojiLevel) {
        this.baojiLevel = baojiLevel;
    }

    /**
     * 获取车队队员身份，0：老板，1：暴鸡，2：暴娘，9：队长
     *
     * @return user_identity - 车队队员身份，0：老板，1：暴鸡，2：暴娘，9：队长
     */
    public Byte getUserIdentity() {
        return userIdentity;
    }

    /**
     * 设置车队队员身份，0：老板，1：暴鸡，2：暴娘，9：队长
     *
     * @param userIdentity 车队队员身份，0：老板，1：暴鸡，2：暴娘，9：队长
     */
    public void setUserIdentity(Byte userIdentity) {
        this.userIdentity = userIdentity;
    }

    /**
     * 获取车队队员头像
     *
     * @return avatar - 车队队员头像
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * 设置车队队员头像
     *
     * @param avatar 车队队员头像
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar == null ? null : avatar.trim();
    }

    /**
     * 获取上车时间
     *
     * @return join_time - 上车时间
     */
    public Date getJoinTime() {
        return joinTime;
    }

    /**
     * 设置上车时间
     *
     * @param joinTime 上车时间
     */
    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }

    /**
     * 获取车队队员游戏角色ID
     *
     * @return game_role_id - 车队队员游戏角色ID
     */
    public Long getGameRoleId() {
        return gameRoleId;
    }

    /**
     * 设置车队队员游戏角色ID
     *
     * @param gameRoleId 车队队员游戏角色ID
     */
    public void setGameRoleId(Long gameRoleId) {
        this.gameRoleId = gameRoleId;
    }

    /**
     * 获取车队队员游戏角色名称
     *
     * @return game_role_name - 车队队员游戏角色名称
     */
    public String getGameRoleName() {
        return gameRoleName;
    }

    /**
     * 设置车队队员游戏角色名称
     *
     * @param gameRoleName 车队队员游戏角色名称
     */
    public void setGameRoleName(String gameRoleName) {
        this.gameRoleName = gameRoleName == null ? null : gameRoleName.trim();
    }

    /**
     * 获取副本位置 code
     *
     * @return raid_location_code - 副本位置 code
     */
    public Integer getRaidLocationCode() {
        return raidLocationCode;
    }

    /**
     * 设置副本位置 code
     *
     * @param raidLocationCode 副本位置 code
     */
    public void setRaidLocationCode(Integer raidLocationCode) {
        this.raidLocationCode = raidLocationCode;
    }

    /**
     * 获取副本位置名称
     *
     * @return raid_location_name - 副本位置名称
     */
    public String getRaidLocationName() {
        return raidLocationName;
    }

    /**
     * 设置副本位置名称
     *
     * @param raidLocationName 副本位置名称
     */
    public void setRaidLocationName(String raidLocationName) {
        this.raidLocationName = raidLocationName == null ? null : raidLocationName.trim();
    }

    /**
     * 获取车队队员状态(0: 待支付(只针对老板), 1: 待入团, 2: 已入团)
     *
     * @return status - 车队队员状态(0: 待支付(只针对老板), 1: 待入团, 2: 已入团)
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置车队队员状态(0: 待支付(只针对老板), 1: 待入团, 2: 已入团)
     *
     * @param status 车队队员状态(0: 待支付(只针对老板), 1: 待入团, 2: 已入团)
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取创建时间
     *
     * @return gmt_create - 创建时间
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * 设置创建时间
     *
     * @param gmtCreate 创建时间
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * 获取修改时间
     *
     * @return gmt_modified - 修改时间
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * 设置修改时间
     *
     * @param gmtModified 修改时间
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}
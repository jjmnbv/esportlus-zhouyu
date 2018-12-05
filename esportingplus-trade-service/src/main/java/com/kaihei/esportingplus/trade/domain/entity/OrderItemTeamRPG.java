package com.kaihei.esportingplus.trade.domain.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "trade_order_item_team_rpg")
public class OrderItemTeamRPG {
    /**
     * 订单明细ID
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 订单用户UID
     */
    private String uid;

    /**
     * 所属订单ID
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 车队序列号
     */
    @Column(name = "team_sequeue")
    private String teamSequeue;

    /**
     * 车队状态，0:未开车，1:已开车
     */
    @Column(name = "team_status")
    private Byte teamStatus;

    /**
     * 队员身份，0：老板，1：暴鸡，2：暴娘，3：队长
     */
    @Column(name = "user_identity")
    private Byte userIdentity;

    /**
     * 暴鸡等级 100: 普通暴鸡 101: 优选暴鸡 102: 超级暴鸡 300: 暴娘
     */
    @Column(name = "user_baoji_level")
    private Integer userBaojiLevel;

    /**
     * 车队队员订单状态(0: 待入团，被队长踢出车队, 1: 待入团，主动退出, 2: 待入团，队长解散车队, 3: 已入团，被队长踢出车队, 4: 已入团，主动退出, 5: 已入团，队长解散车队, 6: 已开车，主动退出, 7: 已开车，队长解散车队，8: 已开车，队长结束车队， -1: 未知状态，忽略处理）
     */
    @Column(name = "user_status")
    private Byte userStatus;

    /**
     * 游戏code
     */
    @Column(name = "game_code")
    private Integer gameCode;

    /**
     * 游戏名称
     */
    @Column(name = "game_name")
    private String gameName;

    /**
     * 副本code
     */
    @Column(name = "raid_code")
    private Integer raidCode;

    /**
     * 副本名称
     */
    @Column(name = "raid_name")
    private String raidName;

    /**
     * 跨区code
     */
    @Column(name = "zone_across_code")
    private Integer zoneAcrossCode;

    /**
     * 跨区名称
     */
    @Column(name = "zone_across_name")
    private String zoneAcrossName;

    /**
     * 游戏角色ID
     */
    @Column(name = "game_role_id")
    private Long gameRoleId;

    /**
     * 游戏角色名称
     */
    @Column(name = "game_role_name")
    private String gameRoleName;

    /**
     * 副本位置code,1: dps 2:辅助
     */
    @Column(name = "raid_location_code")
    private Integer raidLocationCode;

    /**
     * 副本位置名称
     */
    @Column(name = "raid_location_name")
    private String raidLocationName;

    /**
     * 暴鸡收益，单位：分
     */
    private Integer price;

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

    public OrderItemTeamRPG(Long id, String uid, Long orderId, String teamSequeue, Byte teamStatus, Byte userIdentity, Integer userBaojiLevel, Byte userStatus, Integer gameCode, String gameName, Integer raidCode, String raidName, Integer zoneAcrossCode, String zoneAcrossName, Long gameRoleId, String gameRoleName, Integer raidLocationCode, String raidLocationName, Integer price, Date gmtCreate, Date gmtModified) {
        this.id = id;
        this.uid = uid;
        this.orderId = orderId;
        this.teamSequeue = teamSequeue;
        this.teamStatus = teamStatus;
        this.userIdentity = userIdentity;
        this.userBaojiLevel = userBaojiLevel;
        this.userStatus = userStatus;
        this.gameCode = gameCode;
        this.gameName = gameName;
        this.raidCode = raidCode;
        this.raidName = raidName;
        this.zoneAcrossCode = zoneAcrossCode;
        this.zoneAcrossName = zoneAcrossName;
        this.gameRoleId = gameRoleId;
        this.gameRoleName = gameRoleName;
        this.raidLocationCode = raidLocationCode;
        this.raidLocationName = raidLocationName;
        this.price = price;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public OrderItemTeamRPG() {
        super();
    }

    /**
     * 获取订单明细ID
     *
     * @return id - 订单明细ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置订单明细ID
     *
     * @param id 订单明细ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取订单用户UID
     *
     * @return uid - 订单用户UID
     */
    public String getUid() {
        return uid;
    }

    /**
     * 设置订单用户UID
     *
     * @param uid 订单用户UID
     */
    public void setUid(String uid) {
        this.uid = uid == null ? null : uid.trim();
    }

    /**
     * 获取所属订单ID
     *
     * @return order_id - 所属订单ID
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 设置所属订单ID
     *
     * @param orderId 所属订单ID
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取车队序列号
     *
     * @return team_sequeue - 车队序列号
     */
    public String getTeamSequeue() {
        return teamSequeue;
    }

    /**
     * 设置车队序列号
     *
     * @param teamSequeue 车队序列号
     */
    public void setTeamSequeue(String teamSequeue) {
        this.teamSequeue = teamSequeue == null ? null : teamSequeue.trim();
    }

    /**
     * 获取车队状态，0:未开车，1:已开车
     *
     * @return team_status - 车队状态，0:未开车，1:已开车
     */
    public Byte getTeamStatus() {
        return teamStatus;
    }

    /**
     * 设置车队状态，0:未开车，1:已开车
     *
     * @param teamStatus 车队状态，0:未开车，1:已开车
     */
    public void setTeamStatus(Byte teamStatus) {
        this.teamStatus = teamStatus;
    }

    /**
     * 获取队员身份，0：老板，1：暴鸡，2：暴娘，3：队长
     *
     * @return user_identity - 队员身份，0：老板，1：暴鸡，2：暴娘，3：队长
     */
    public Byte getUserIdentity() {
        return userIdentity;
    }

    /**
     * 设置队员身份，0：老板，1：暴鸡，2：暴娘，3：队长
     *
     * @param userIdentity 队员身份，0：老板，1：暴鸡，2：暴娘，3：队长
     */
    public void setUserIdentity(Byte userIdentity) {
        this.userIdentity = userIdentity;
    }

    /**
     * 获取暴鸡等级 100: 普通暴鸡 101: 优选暴鸡 102: 超级暴鸡 300: 暴娘
     *
     * @return user_baoji_level - 暴鸡等级 100: 普通暴鸡 101: 优选暴鸡 102: 超级暴鸡 300: 暴娘
     */
    public Integer getUserBaojiLevel() {
        return userBaojiLevel;
    }

    /**
     * 设置暴鸡等级 100: 普通暴鸡 101: 优选暴鸡 102: 超级暴鸡 300: 暴娘
     *
     * @param userBaojiLevel 暴鸡等级 100: 普通暴鸡 101: 优选暴鸡 102: 超级暴鸡 300: 暴娘
     */
    public void setUserBaojiLevel(Integer userBaojiLevel) {
        this.userBaojiLevel = userBaojiLevel;
    }

    /**
     * 获取车队队员订单状态(0: 待入团，被队长踢出车队, 1: 待入团，主动退出, 2: 待入团，队长解散车队, 3: 已入团，被队长踢出车队, 4: 已入团，主动退出, 5: 已入团，队长解散车队, 6: 已开车，主动退出, 7: 已开车，队长解散车队，8: 已开车，队长结束车队， -1: 未知状态，忽略处理）
     *
     * @return user_status - 车队队员订单状态(0: 待入团，被队长踢出车队, 1: 待入团，主动退出, 2: 待入团，队长解散车队, 3: 已入团，被队长踢出车队, 4: 已入团，主动退出, 5: 已入团，队长解散车队, 6: 已开车，主动退出, 7: 已开车，队长解散车队，8: 已开车，队长结束车队， -1: 未知状态，忽略处理）
     */
    public Byte getUserStatus() {
        return userStatus;
    }

    /**
     * 设置车队队员订单状态(0: 待入团，被队长踢出车队, 1: 待入团，主动退出, 2: 待入团，队长解散车队, 3: 已入团，被队长踢出车队, 4: 已入团，主动退出, 5: 已入团，队长解散车队, 6: 已开车，主动退出, 7: 已开车，队长解散车队，8: 已开车，队长结束车队， -1: 未知状态，忽略处理）
     *
     * @param userStatus 车队队员订单状态(0: 待入团，被队长踢出车队, 1: 待入团，主动退出, 2: 待入团，队长解散车队, 3: 已入团，被队长踢出车队, 4: 已入团，主动退出, 5: 已入团，队长解散车队, 6: 已开车，主动退出, 7: 已开车，队长解散车队，8: 已开车，队长结束车队， -1: 未知状态，忽略处理）
     */
    public void setUserStatus(Byte userStatus) {
        this.userStatus = userStatus;
    }

    /**
     * 获取游戏code
     *
     * @return game_code - 游戏code
     */
    public Integer getGameCode() {
        return gameCode;
    }

    /**
     * 设置游戏code
     *
     * @param gameCode 游戏code
     */
    public void setGameCode(Integer gameCode) {
        this.gameCode = gameCode;
    }

    /**
     * 获取游戏名称
     *
     * @return game_name - 游戏名称
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * 设置游戏名称
     *
     * @param gameName 游戏名称
     */
    public void setGameName(String gameName) {
        this.gameName = gameName == null ? null : gameName.trim();
    }

    /**
     * 获取副本code
     *
     * @return raid_code - 副本code
     */
    public Integer getRaidCode() {
        return raidCode;
    }

    /**
     * 设置副本code
     *
     * @param raidCode 副本code
     */
    public void setRaidCode(Integer raidCode) {
        this.raidCode = raidCode;
    }

    /**
     * 获取副本名称
     *
     * @return raid_name - 副本名称
     */
    public String getRaidName() {
        return raidName;
    }

    /**
     * 设置副本名称
     *
     * @param raidName 副本名称
     */
    public void setRaidName(String raidName) {
        this.raidName = raidName == null ? null : raidName.trim();
    }

    /**
     * 获取跨区code
     *
     * @return zone_across_code - 跨区code
     */
    public Integer getZoneAcrossCode() {
        return zoneAcrossCode;
    }

    /**
     * 设置跨区code
     *
     * @param zoneAcrossCode 跨区code
     */
    public void setZoneAcrossCode(Integer zoneAcrossCode) {
        this.zoneAcrossCode = zoneAcrossCode;
    }

    /**
     * 获取跨区名称
     *
     * @return zone_across_name - 跨区名称
     */
    public String getZoneAcrossName() {
        return zoneAcrossName;
    }

    /**
     * 设置跨区名称
     *
     * @param zoneAcrossName 跨区名称
     */
    public void setZoneAcrossName(String zoneAcrossName) {
        this.zoneAcrossName = zoneAcrossName == null ? null : zoneAcrossName.trim();
    }

    /**
     * 获取游戏角色ID
     *
     * @return game_role_id - 游戏角色ID
     */
    public Long getGameRoleId() {
        return gameRoleId;
    }

    /**
     * 设置游戏角色ID
     *
     * @param gameRoleId 游戏角色ID
     */
    public void setGameRoleId(Long gameRoleId) {
        this.gameRoleId = gameRoleId;
    }

    /**
     * 获取游戏角色名称
     *
     * @return game_role_name - 游戏角色名称
     */
    public String getGameRoleName() {
        return gameRoleName;
    }

    /**
     * 设置游戏角色名称
     *
     * @param gameRoleName 游戏角色名称
     */
    public void setGameRoleName(String gameRoleName) {
        this.gameRoleName = gameRoleName == null ? null : gameRoleName.trim();
    }

    /**
     * 获取副本位置code,1: dps 2:辅助
     *
     * @return raid_location_code - 副本位置code,1: dps 2:辅助
     */
    public Integer getRaidLocationCode() {
        return raidLocationCode;
    }

    /**
     * 设置副本位置code,1: dps 2:辅助
     *
     * @param raidLocationCode 副本位置code,1: dps 2:辅助
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
     * 获取暴鸡收益，单位：分
     *
     * @return price - 暴鸡收益，单位：分
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * 设置暴鸡收益，单位：分
     *
     * @param price 暴鸡收益，单位：分
     */
    public void setPrice(Integer price) {
        this.price = price;
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
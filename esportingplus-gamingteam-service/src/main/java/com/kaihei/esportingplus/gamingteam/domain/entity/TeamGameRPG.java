package com.kaihei.esportingplus.gamingteam.domain.entity;

import java.util.Date;
import javax.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Table(name = "team_game_rpg")
public class TeamGameRPG {
    /**
     * 车队游戏信息唯一标识
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 车队ID
     */
    @Column(name = "team_id")
    private Long teamId;

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
     * 攻坚队名称，20 字符以内
     */
    @Column(name = "assault_name")
    private String assaultName;

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
     * 游戏跨区code，如：跨二区的code
     */
    @Column(name = "zone_across_code")
    private Integer zoneAcrossCode;

    /**
     * 游戏跨区名称，如：跨二区
     */
    @Column(name = "zone_across_name")
    private String zoneAcrossName;

    /**
     * 游戏频道，20 字符以内
     */
    private String channel;

    /**
     * 车队原价, 单位: 分
     */
    @Column(name = "original_fee")
    private Integer originalFee;

    /**
     * 车队折扣价, 单位: 分
     */
    @Column(name = "discount_fee")
    private Integer discountFee;

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

    public TeamGameRPG(Long id, Long teamId, Integer gameCode, String gameName, String assaultName, Integer raidCode, String raidName, Integer zoneAcrossCode, String zoneAcrossName, String channel, Integer originalFee, Integer discountFee, Date gmtCreate, Date gmtModified) {
        this.id = id;
        this.teamId = teamId;
        this.gameCode = gameCode;
        this.gameName = gameName;
        this.assaultName = assaultName;
        this.raidCode = raidCode;
        this.raidName = raidName;
        this.zoneAcrossCode = zoneAcrossCode;
        this.zoneAcrossName = zoneAcrossName;
        this.channel = channel;
        this.originalFee = originalFee;
        this.discountFee = discountFee;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public TeamGameRPG() {
        super();
    }

    /**
     * 获取车队游戏信息唯一标识
     *
     * @return id - 车队游戏信息唯一标识
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置车队游戏信息唯一标识
     *
     * @param id 车队游戏信息唯一标识
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取车队ID
     *
     * @return team_id - 车队ID
     */
    public Long getTeamId() {
        return teamId;
    }

    /**
     * 设置车队ID
     *
     * @param teamId 车队ID
     */
    public void setTeamId(Long teamId) {
        this.teamId = teamId;
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
     * 获取攻坚队名称，20 字符以内
     *
     * @return assault_name - 攻坚队名称，20 字符以内
     */
    public String getAssaultName() {
        return assaultName;
    }

    /**
     * 设置攻坚队名称，20 字符以内
     *
     * @param assaultName 攻坚队名称，20 字符以内
     */
    public void setAssaultName(String assaultName) {
        this.assaultName = assaultName == null ? null : assaultName.trim();
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
     * 获取游戏跨区code，如：跨二区的code
     *
     * @return zone_across_code - 游戏跨区code，如：跨二区的code
     */
    public Integer getZoneAcrossCode() {
        return zoneAcrossCode;
    }

    /**
     * 设置游戏跨区code，如：跨二区的code
     *
     * @param zoneAcrossCode 游戏跨区code，如：跨二区的code
     */
    public void setZoneAcrossCode(Integer zoneAcrossCode) {
        this.zoneAcrossCode = zoneAcrossCode;
    }

    /**
     * 获取游戏跨区名称，如：跨二区
     *
     * @return zone_across_name - 游戏跨区名称，如：跨二区
     */
    public String getZoneAcrossName() {
        return zoneAcrossName;
    }

    /**
     * 设置游戏跨区名称，如：跨二区
     *
     * @param zoneAcrossName 游戏跨区名称，如：跨二区
     */
    public void setZoneAcrossName(String zoneAcrossName) {
        this.zoneAcrossName = zoneAcrossName == null ? null : zoneAcrossName.trim();
    }

    /**
     * 获取游戏频道，20 字符以内
     *
     * @return channel - 游戏频道，20 字符以内
     */
    public String getChannel() {
        return channel;
    }

    /**
     * 设置游戏频道，20 字符以内
     *
     * @param channel 游戏频道，20 字符以内
     */
    public void setChannel(String channel) {
        this.channel = channel == null ? null : channel.trim();
    }

    /**
     * 获取车队原价, 单位: 分
     *
     * @return original_fee - 车队原价, 单位: 分
     */
    public Integer getOriginalFee() {
        return originalFee;
    }

    /**
     * 设置车队原价, 单位: 分
     *
     * @param originalFee 车队原价, 单位: 分
     */
    public void setOriginalFee(Integer originalFee) {
        this.originalFee = originalFee;
    }

    /**
     * 获取车队折扣价, 单位: 分
     *
     * @return discount_fee - 车队折扣价, 单位: 分
     */
    public Integer getDiscountFee() {
        return discountFee;
    }

    /**
     * 设置车队折扣价, 单位: 分
     *
     * @param discountFee 车队折扣价, 单位: 分
     */
    public void setDiscountFee(Integer discountFee) {
        this.discountFee = discountFee;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
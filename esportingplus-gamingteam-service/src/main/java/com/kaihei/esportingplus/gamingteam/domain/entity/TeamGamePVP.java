package com.kaihei.esportingplus.gamingteam.domain.entity;

import com.kaihei.esportingplus.common.data.Castable;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.ToString;

@ToString
@Builder
@Table(name = "team_game_pvp")
public class TeamGamePVP implements Castable, TeamGame {

    /**
     * PVP车队游戏信息主键ID
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
     * 车队所属游戏ID
     */
    @Column(name = "game_id")
    private Integer gameId;

    /**
     * 车队所属游戏名称
     */
    @Column(name = "game_name")
    private String gameName;

    /**
     * 车队所属游戏大区ID
     */
    @Column(name = "game_zone_id")
    private Integer gameZoneId;

    /**
     * 车队所属游戏大区名称
     */
    @Column(name = "game_zone_name")
    private String gameZoneName;

    /**
     * 段位下限ID
     */
    @Column(name = "lower_dan_id")
    private Integer lowerDanId;

    /**
     * 段位下限名称
     */
    @Column(name = "lower_dan_name")
    private String lowerDanName;

    /**
     * 段位上限ID
     */
    @Column(name = "upper_dan_id")
    private Integer upperDanId;

    /**
     * 段位上限名称
     */
    @Column(name = "upper_dan_name")
    private String upperDanName;

    /**
     * 车队初始价格(按暴鸡队长的算), 单位: 分
     */
    @Column(name = "init_fee")
    private Integer initFee;

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

    public TeamGamePVP(Long id, Long teamId, Integer gameId, String gameName, Integer gameZoneId, String gameZoneName, Integer lowerDanId, String lowerDanName, Integer upperDanId, String upperDanName, Integer initFee, Integer discountFee, Date gmtCreate, Date gmtModified) {
        this.id = id;
        this.teamId = teamId;
        this.gameId = gameId;
        this.gameName = gameName;
        this.gameZoneId = gameZoneId;
        this.gameZoneName = gameZoneName;
        this.lowerDanId = lowerDanId;
        this.lowerDanName = lowerDanName;
        this.upperDanId = upperDanId;
        this.upperDanName = upperDanName;
        this.initFee = initFee;
        this.discountFee = discountFee;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public TeamGamePVP() {
        super();
    }

    /**
     * 获取PVP车队游戏信息主键ID
     *
     * @return id - PVP车队游戏信息主键ID
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * 设置PVP车队游戏信息主键ID
     *
     * @param id PVP车队游戏信息主键ID
     */
    @Override
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
    @Override
    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    /**
     * 获取车队所属游戏ID
     *
     * @return game_id - 车队所属游戏ID
     */
    @Override
    public Integer getGameId() {
        return gameId;
    }

    /**
     * 设置车队所属游戏ID
     *
     * @param gameId 车队所属游戏ID
     */
    @Override
    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    /**
     * 获取车队所属游戏名称
     *
     * @return game_name - 车队所属游戏名称
     */
    @Override
    public String getGameName() {
        return gameName;
    }

    /**
     * 设置车队所属游戏名称
     *
     * @param gameName 车队所属游戏名称
     */
    @Override
    public void setGameName(String gameName) {
        this.gameName = gameName == null ? null : gameName.trim();
    }

    /**
     * 获取车队所属游戏大区ID
     *
     * @return game_zone_id - 车队所属游戏大区ID
     */
    @Override
    public Integer getGameZoneId() {
        return gameZoneId;
    }

    /**
     * 设置车队所属游戏大区ID
     *
     * @param gameZoneId 车队所属游戏大区ID
     */
    @Override
    public void setGameZoneId(Integer gameZoneId) {
        this.gameZoneId = gameZoneId;
    }

    /**
     * 获取车队所属游戏大区名称
     *
     * @return game_zone_name - 车队所属游戏大区名称
     */
    @Override
    public String getGameZoneName() {
        return gameZoneName;
    }

    /**
     * 设置车队所属游戏大区名称
     *
     * @param gameZoneName 车队所属游戏大区名称
     */
    @Override
    public void setGameZoneName(String gameZoneName) {
        this.gameZoneName = gameZoneName == null ? null : gameZoneName.trim();
    }

    /**
     * 获取段位下限ID
     *
     * @return lower_dan_id - 段位下限ID
     */
    @Override
    public Integer getLowerDanId() {
        return lowerDanId;
    }

    /**
     * 设置段位下限ID
     *
     * @param lowerDanId 段位下限ID
     */
    public void setLowerDanId(Integer lowerDanId) {
        this.lowerDanId = lowerDanId;
    }

    /**
     * 获取段位下限名称
     *
     * @return lower_dan_name - 段位下限名称
     */
    public String getLowerDanName() {
        return lowerDanName;
    }

    /**
     * 设置段位下限名称
     *
     * @param lowerDanName 段位下限名称
     */
    @Override
    public void setLowerDanName(String lowerDanName) {
        this.lowerDanName = lowerDanName == null ? null : lowerDanName.trim();
    }

    /**
     * 获取段位上限ID
     *
     * @return upper_dan_id - 段位上限ID
     */
    @Override
    public Integer getUpperDanId() {
        return upperDanId;
    }

    /**
     * 设置段位上限ID
     *
     * @param upperDanId 段位上限ID
     */
    public void setUpperDanId(Integer upperDanId) {
        this.upperDanId = upperDanId;
    }

    /**
     * 获取段位上限名称
     *
     * @return upper_dan_name - 段位上限名称
     */
    public String getUpperDanName() {
        return upperDanName;
    }

    /**
     * 设置段位上限名称
     *
     * @param upperDanName 段位上限名称
     */
    @Override
    public void setUpperDanName(String upperDanName) {
        this.upperDanName = upperDanName == null ? null : upperDanName.trim();
    }

    /**
     * 获取车队初始价格(按暴鸡队长的算), 单位: 分
     *
     * @return init_fee - 车队初始价格(按暴鸡队长的算), 单位: 分
     */
    public Integer getInitFee() {
        return initFee;
    }

    /**
     * 设置车队初始价格(按暴鸡队长的算), 单位: 分
     *
     * @param initFee 车队初始价格(按暴鸡队长的算), 单位: 分
     */
    public void setInitFee(Integer initFee) {
        this.initFee = initFee;
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
    @Override
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * 获取修改时间
     *
     * @return gmt_modified - 修改时间
     */
    @Override
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * 设置修改时间
     *
     * @param gmtModified 修改时间
     */
    @Override
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}
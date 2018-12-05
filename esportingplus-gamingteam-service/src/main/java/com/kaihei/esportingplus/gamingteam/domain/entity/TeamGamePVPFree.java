package com.kaihei.esportingplus.gamingteam.domain.entity;

import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 免费车队 车队-游戏信息
 * @author liangyi
 */
@Table(name = "team_game_pvp_free")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TeamGamePVPFree implements Serializable, TeamGame {

    private static final long serialVersionUID = -5505333178766472192L;
    /**
     * PVP免费车队游戏信息主键ID
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
     * 免费车队类型ID
     */
    @Column(name = "free_team_type_id")
    private Integer freeTeamTypeId;

    /**
     * 免费车队类型显示名称
     */
    @Column(name = "free_team_type_name")
    private String freeTeamTypeName;

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
     * 创建时间
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    private Date gmtModified;


    /**
     * 获取PVP免费车队游戏信息主键ID
     *
     * @return id - PVP免费车队游戏信息主键ID
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * 设置PVP免费车队游戏信息主键ID
     *
     * @param id PVP免费车队游戏信息主键ID
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

    @Transient
    private Integer gameId;

    @Transient
    private String gameName;

    @Override
    public String getGameName() {
        return gameName;
    }

    @Override
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }


    @Override
    public Integer getGameId() {
        return gameId;
    }

    @Override
    public void setGameId(Integer gameId) {
        this.gameId = gameId;
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
     * 获取免费车队类型ID
     *
     * @return free_team_type_id - 免费车队类型ID
     */
    public Integer getFreeTeamTypeId() {
        return freeTeamTypeId;
    }

    /**
     * 设置免费车队类型ID
     *
     * @param freeTeamTypeId 免费车队类型ID
     */
    public void setFreeTeamTypeId(Integer freeTeamTypeId) {
        this.freeTeamTypeId = freeTeamTypeId;
    }

    /**
     * 获取免费车队类型显示名称
     *
     * @return free_team_type_name - 免费车队类型显示名称
     */
    public String getFreeTeamTypeName() {
        return freeTeamTypeName;
    }

    /**
     * 设置免费车队类型显示名称
     *
     * @param freeTeamTypeName 免费车队类型显示名称
     */
    public void setFreeTeamTypeName(String freeTeamTypeName) {
        this.freeTeamTypeName = freeTeamTypeName == null ? null : freeTeamTypeName.trim();
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
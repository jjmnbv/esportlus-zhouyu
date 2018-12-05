package com.kaihei.esportingplus.user.domain.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "user_game_role_credential")
public class UserGameRoleCredential {
    /**
     * 角色认证ID
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 所属角色ID
     */
    @Column(name = "game_role_id")
    private Long gameRoleId;

    /**
     * 副本类型code
     */
    @Column(name = "raid_code")
    private Integer raidCode;

    /**
     * 副本类型名称
     */
    @Column(name = "raid_name")
    private String raidName;

    /**
     * 游戏位置code
     */
    @Column(name = "raid_location_code")
    private Integer raidLocationCode;

    /**
     * 游戏位置名称
     */
    @Column(name = "raid_location_name")
    private String raidLocationName;

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

    public UserGameRoleCredential(Long id, Long gameRoleId, Integer raidCode, String raidName, Integer raidLocationCode, String raidLocationName, Date gmtCreate, Date gmtModified) {
        this.id = id;
        this.gameRoleId = gameRoleId;
        this.raidCode = raidCode;
        this.raidName = raidName;
        this.raidLocationCode = raidLocationCode;
        this.raidLocationName = raidLocationName;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public UserGameRoleCredential() {
        super();
    }

    /**
     * 获取角色认证ID
     *
     * @return id - 角色认证ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置角色认证ID
     *
     * @param id 角色认证ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取所属角色ID
     *
     * @return game_role_id - 所属角色ID
     */
    public Long getGameRoleId() {
        return gameRoleId;
    }

    /**
     * 设置所属角色ID
     *
     * @param gameRoleId 所属角色ID
     */
    public void setGameRoleId(Long gameRoleId) {
        this.gameRoleId = gameRoleId;
    }

    /**
     * 获取副本类型code
     *
     * @return raid_code - 副本类型code
     */
    public Integer getRaidCode() {
        return raidCode;
    }

    /**
     * 设置副本类型code
     *
     * @param raidCode 副本类型code
     */
    public void setRaidCode(Integer raidCode) {
        this.raidCode = raidCode;
    }

    /**
     * 获取副本类型名称
     *
     * @return raid_name - 副本类型名称
     */
    public String getRaidName() {
        return raidName;
    }

    /**
     * 设置副本类型名称
     *
     * @param raidName 副本类型名称
     */
    public void setRaidName(String raidName) {
        this.raidName = raidName == null ? null : raidName.trim();
    }

    /**
     * 获取游戏位置code
     *
     * @return raid_location_code - 游戏位置code
     */
    public Integer getRaidLocationCode() {
        return raidLocationCode;
    }

    /**
     * 设置游戏位置code
     *
     * @param raidLocationCode 游戏位置code
     */
    public void setRaidLocationCode(Integer raidLocationCode) {
        this.raidLocationCode = raidLocationCode;
    }

    /**
     * 获取游戏位置名称
     *
     * @return raid_location_name - 游戏位置名称
     */
    public String getRaidLocationName() {
        return raidLocationName;
    }

    /**
     * 设置游戏位置名称
     *
     * @param raidLocationName 游戏位置名称
     */
    public void setRaidLocationName(String raidLocationName) {
        this.raidLocationName = raidLocationName == null ? null : raidLocationName.trim();
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
package com.kaihei.esportingplus.user.domain.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "user_game_role")
public class UserGameRole {
    /**
     * 角色ID
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 游戏code
     */
    @Column(name = "game_code")
    private Integer gameCode;

    /**
     * 所属用户UID
     */
    @Column(name = "uid")
    private String uid;

    /**
     * 角色名
     */
    private String name;

    /**
     * 角色头像
     */
    private String avatar;

    /**
     * 一级职业code
     */
    @Column(name = "career_code")
    private String careerCode;

    /**
     * 一级职业名称
     */
    @Column(name = "career_name")
    private String careerName;

    /**
     * 服务器大区code
     */
    @Column(name = "zone_big_code")
    private Integer zoneBigCode;

    /**
     * 服务器小区code
     */
    @Column(name = "zone_small_code")
    private Integer zoneSmallCode;

    /**
     * 服务器大区名称
     */
    @Column(name = "zone_big_name")
    private String zoneBigName;

    /**
     * 服务器小区名称
     */
    @Column(name = "zone_small_name")
    private String zoneSmallName;

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

    public UserGameRole(Long id, Integer gameCode, String uid, String name, String avatar, String careerCode,  String careerName, Integer zoneBigCode, Integer zoneSmallCode, String zoneBigName, String zoneSmallName, Date gmtCreate, Date gmtModified) {
        this.id = id;
        this.gameCode = gameCode;
        this.uid = uid;
        this.name = name;
        this.avatar = avatar;
        this.careerCode = careerCode;
        this.careerName = careerName;
        this.zoneBigCode = zoneBigCode;
        this.zoneSmallCode = zoneSmallCode;
        this.zoneBigName = zoneBigName;
        this.zoneSmallName = zoneSmallName;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public UserGameRole() {
        super();
    }

    /**
     * 获取角色ID
     *
     * @return id - 角色ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置角色ID
     *
     * @param id 角色ID
     */
    public void setId(Long id) {
        this.id = id;
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
     * 获取所属用户UID
     *
     * @return user_uid - 所属用户UID
     */
    public String getUid() {
        return uid;
    }

    /**
     * 设置所属用户UID
     *
     * @param uid 所属用户UID
     */
    public void setUid(String uid) {
        this.uid = uid == null ? null : uid.trim();
    }

    /**
     * 获取角色名
     *
     * @return name - 角色名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置角色名
     *
     * @param name 角色名
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取角色头像
     *
     * @return avatar - 角色头像
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * 设置角色头像
     *
     * @param avatar 角色头像
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar == null ? null : avatar.trim();
    }

    /**
     * 获取一级职业code
     *
     * @return career1_code - 一级职业code
     */
    public String getCareerCode() {
        return careerCode;
    }

    /**
     * 设置一级职业code
     *
     * @param careerCode 一级职业code
     */
    public void setCareerCode(String careerCode) {
        this.careerCode = careerCode;
    }


    /**
     * 获取一级职业名称
     *
     * @return career1_name - 一级职业名称
     */
    public String getCareerName() {
        return careerName;
    }

    /**
     * 设置一级职业名称
     *
     * @param careerName 一级职业名称
     */
    public void setCareerName(String careerName) {
        this.careerName = careerName == null ? null : careerName.trim();
    }


    /**
     * 获取服务器大区code
     *
     * @return zone_big_code - 服务器大区code
     */
    public Integer getZoneBigCode() {
        return zoneBigCode;
    }

    /**
     * 设置服务器大区code
     *
     * @param zoneBigCode 服务器大区code
     */
    public void setZoneBigCode(Integer zoneBigCode) {
        this.zoneBigCode = zoneBigCode;
    }

    /**
     * 获取服务器小区code
     *
     * @return zone_small_code - 服务器小区code
     */
    public Integer getZoneSmallCode() {
        return zoneSmallCode;
    }

    /**
     * 设置服务器小区code
     *
     * @param zoneSmallCode 服务器小区code
     */
    public void setZoneSmallCode(Integer zoneSmallCode) {
        this.zoneSmallCode = zoneSmallCode;
    }

    /**
     * 获取服务器大区名称
     *
     * @return zone_big_name - 服务器大区名称
     */
    public String getZoneBigName() {
        return zoneBigName;
    }

    /**
     * 设置服务器大区名称
     *
     * @param zoneBigName 服务器大区名称
     */
    public void setZoneBigName(String zoneBigName) {
        this.zoneBigName = zoneBigName == null ? null : zoneBigName.trim();
    }

    /**
     * 获取服务器小区名称
     *
     * @return zone_small_name - 服务器小区名称
     */
    public String getZoneSmallName() {
        return zoneSmallName;
    }

    /**
     * 设置服务器小区名称
     *
     * @param zoneSmallName 服务器小区名称
     */
    public void setZoneSmallName(String zoneSmallName) {
        this.zoneSmallName = zoneSmallName == null ? null : zoneSmallName.trim();
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
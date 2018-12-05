package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;

public class UserGameBaseRoleInfoVo implements Serializable {

    private static final long serialVersionUID = -8087882456236842624L;
    /**
     * 角色ID
     */
    private Long id;

    /**
     * 游戏code
     */
    private Integer gameCode;


    /**
     * 角色名
     */
    private String name;

    /**
     * 角色头像
     */
    private String avatar;

    /**
     * 职业code
     */
    private String careerCode;
    /**
     * 职业名称
     */
    private String careerName;

    /**
     * 服务器大区code
     */
    private Integer zoneBigCode;

    /**
     * 服务器小区code
     */
    private Integer zoneSmallCode;

    /**
     * 服务器大区名称
     */
    private String zoneBigName;

    /**
     * 服务器小区名称
     */
    private String zoneSmallName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGameCode() {
        return gameCode;
    }

    public void setGameCode(Integer gameCode) {
        this.gameCode = gameCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public Integer getZoneBigCode() {
        return zoneBigCode;
    }

    public void setZoneBigCode(Integer zoneBigCode) {
        this.zoneBigCode = zoneBigCode;
    }

    public Integer getZoneSmallCode() {
        return zoneSmallCode;
    }

    public void setZoneSmallCode(Integer zoneSmallCode) {
        this.zoneSmallCode = zoneSmallCode;
    }

    public String getZoneBigName() {
        return zoneBigName;
    }

    public void setZoneBigName(String zoneBigName) {
        this.zoneBigName = zoneBigName;
    }

    public String getZoneSmallName() {
        return zoneSmallName;
    }

    public void setZoneSmallName(String zoneSmallName) {
        this.zoneSmallName = zoneSmallName;
    }
}

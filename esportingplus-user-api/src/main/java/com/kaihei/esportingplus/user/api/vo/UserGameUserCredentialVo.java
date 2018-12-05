package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;

public class UserGameUserCredentialVo implements Serializable {

    private static final long serialVersionUID = 9054722538243588956L;
    /**
     * 副本类型ID
     */
    private Integer raidCode;

    /**
     * 副本类型名称
     */
    private String raidName;

    /**
     * 游戏位置ID
     */
    private Integer raidLocationCode;

    /**
     * 游戏位置名称
     */
    private String raidLocationName;
    /**
     * 暴鸡等级
     */
    private Integer baojiLevel;
    /**
     * 暴鸡等级名称
     */
    private String baojiName;
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

    public Integer getBaojiLevel() {
        return baojiLevel;
    }

    public void setBaojiLevel(Integer baojiLevel) {
        this.baojiLevel = baojiLevel;
    }

    public String getBaojiName() {
        return baojiName;
    }

    public void setBaojiName(String baojiName) {
        this.baojiName = baojiName;
    }
}

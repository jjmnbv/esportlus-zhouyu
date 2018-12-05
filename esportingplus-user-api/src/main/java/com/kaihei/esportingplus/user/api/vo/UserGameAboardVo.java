package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;

/**
 * 用户上车角色查询返回实体
 *
 * @author 张方
 */
public class UserGameAboardVo implements Serializable {

    private static final long serialVersionUID = -249209498334414073L;
    /**
     * 角色Id
     **/
    private Long roleId;
    /**
     * 角色名
     **/
    private String roleName;
    /**
     * 暴鸡等级
     **/
    private Integer baojiLevel;
    /**
     * 职业code
     **/
    private Integer careerCode;
    /**
     * 职业名称
     **/
    private String careerName;

    /**
     * 游戏位置ID
     */
    private Integer raidLocationCode;

    /**
     * 游戏位置名称
     */
    private String raidLocationName;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getBaojiLevel() {
        return baojiLevel;
    }

    public void setBaojiLevel(Integer baojiLevel) {
        this.baojiLevel = baojiLevel;
    }

    public Integer getCareerCode() {
        return careerCode;
    }

    public void setCareerCode(Integer careerCode) {
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
}

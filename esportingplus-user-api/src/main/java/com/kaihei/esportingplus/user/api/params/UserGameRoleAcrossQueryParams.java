package com.kaihei.esportingplus.user.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 提供给用户上车时获取角色列表的查询参数
 * @author liangyi
 */
@ApiModel("用户上车时获取角色列表的查询参数")
public class UserGameRoleAcrossQueryParams {

    /**
     * 用户 uid
     */
    @ApiModelProperty(value = "用户uid", required = true, position = 1, example = "")
    @NotBlank(message = "用户uid不能为空")
    private String uid;

    /**
     * 游戏 code
     */
    @ApiModelProperty(value = "游戏code", required = true, position = 1, example = "")
    @NotNull(message = "游戏code不能为空")
    private Integer gameCode;

    /**
     * 副本 code
     */
    @ApiModelProperty(value = "副本code", required = true, position = 1, example = "")
    @NotNull(message = "副本code不能为空")
    private Integer raidCode;

    /**
     * 服务跨区 code
     */
    @ApiModelProperty(value = "服务跨区code", required = true, position = 1, example = "")
    @NotNull(message = "跨区code不能为空")
    private Integer zoneAcrossCode;

    /**
     * 用户身份标识 1:暴鸡, 2:暴娘, 0:老板
     */
    @ApiModelProperty(value = "用户身份", required = true, position = 1, example = "")
    @NotNull(message = "用户身份不能为空")
    private Integer userIdentity;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getGameCode() {
        return gameCode;
    }

    public void setGameCode(Integer gameCode) {
        this.gameCode = gameCode;
    }

    public Integer getRaidCode() {
        return raidCode;
    }

    public void setRaidCode(Integer raidCode) {
        this.raidCode = raidCode;
    }

    public Integer getZoneAcrossCode() {
        return zoneAcrossCode;
    }

    public void setZoneAcrossCode(Integer zoneAcrossCode) {
        this.zoneAcrossCode = zoneAcrossCode;
    }

    public Integer getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(Integer userIdentity) {
        this.userIdentity = userIdentity;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

package com.kaihei.esportingplus.user.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

/**
 * 提供给用户上车时获取老板或暴鸡角色
 * @author liangyi
 */
@Validated
@ApiModel("用户上车时检查角色参数")
public class UserSingeRoleQueryParams implements Serializable {

    private static final long serialVersionUID = -638781722561272493L;
    @ApiModelProperty(value = "用户uid", required = true, position = 1, example = "")
    @NotBlank(message = "用户uid不能为空")
    private String uid;
    @ApiModelProperty(value = "游戏code", required = true, position = 1, example = "")
    @NotNull(message = "游戏code不能为空")
    private Integer gameCode;
    @ApiModelProperty(value = "角色id", required = true, position = 1, example = "")
    @NotNull(message = "角色id不能为空")
    private Long roleId;
    @ApiModelProperty(value = "用户身份", required = true, position = 1, example = "")
    @NotNull(message = "用户身份不能为空")
    private Integer userIdentity;

    @ApiModelProperty(value = "副本code", required = true, position = 1, example = "")
    @NotNull(message = "副本code不能为空")
    private Integer raidCode;

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

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Integer getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(Integer userIdentity) {
        this.userIdentity = userIdentity;
    }

    public Integer getRaidCode() {
        return raidCode;
    }

    public void setRaidCode(Integer raidCode) {
        this.raidCode = raidCode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

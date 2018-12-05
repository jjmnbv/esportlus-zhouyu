package com.kaihei.esportingplus.gamingteam.api.params.rpg;

import com.kaihei.esportingplus.common.data.Castable;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

/**
 * 车队基本输入参数
 * @author liangyi
 */
@Validated
public class RPGTeamMemberBaseParams implements Castable {

    /**
     * 用户身份
     * 0: 老板, 1: 暴鸡, 2: 暴娘
     */
    @NotNull(message = "用户身份不能为空")
    @Range(min = 0, max = 2, message = "用户身份范围为0~2")
    @ApiModelProperty(value = "用户身份", required = true, position = 1, example = "0")
    private Integer userIdentity;

    /** 游戏角色 id */
    @NotNull(message = "游戏角色id不能为空")
    @Min(value = 1, message = "游戏角色id不能小于1")
    @ApiModelProperty(value = "游戏角色id", required = true, position = 1, example = "6")
    private Long gameRoleId;

    public RPGTeamMemberBaseParams() {
    }

    public Integer getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(Integer userIdentity) {
        this.userIdentity = userIdentity;
    }

    public Long getGameRoleId() {
        return gameRoleId;
    }

    public void setGameRoleId(Long gameRoleId) {
        this.gameRoleId = gameRoleId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

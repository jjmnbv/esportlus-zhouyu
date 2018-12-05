package com.kaihei.esportingplus.gamingteam.api.params.rpg;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

/**
 * 根据用户身份获取符合该车队的角色列表参数
 * @author liangyi
 */
@Validated
@ApiModel(value = "获取符合该车队的角色列表", description = "获取角色列表参数")
public class RPGTeamRolesParams {

    /** 车队序列号 */
    @NotEmpty(message = "车队序列号不能为空")
    @ApiModelProperty(value = "车队序列号", required = true, position = 1, example = "225298448138764288")
    private String sequence;

    /**
     * 用户身份
     * 0: 老板, 1: 暴鸡, 2: 暴娘
     */
    @NotNull(message = "用户身份不能为空")
    @Range(min = 0, max = 2, message = "用户身份范围为0~2")
    @ApiModelProperty(value = "用户身份", required = true, position = 1, example = "0")
    private Integer userIdentity;

    public RPGTeamRolesParams() {
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public Integer getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(Integer userIdentity) {
        this.userIdentity = userIdentity;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

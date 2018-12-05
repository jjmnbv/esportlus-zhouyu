package com.kaihei.esportingplus.gamingteam.api.params.pvp;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class PVPTeamJoinParams {

    /**
     * 车队序列号
     */
    @NotEmpty(message = "车队序列号不能为空")
    @ApiModelProperty(value = "车队序列号", required = true, position = 1, example = "225298448138764288")
    private String sequence;

    /**
     * 用户身份 0: 老板, 1: 暴鸡, 2: 暴娘
     */
    @NotNull(message = "用户身份不能为空")
    @Range(min = 0, max = 2, message = "用户身份范围为0~2")
    @ApiModelProperty(value = "用户身份", required = true, position = 1, example = "0")
    private Integer userIdentity;
}

package com.kaihei.esportingplus.gamingteam.api.params.rpg;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

/**
 * 加入车队参数
 * @author liangyi
 */
@Validated
@ApiModel(value = "加入车队", description = "加入车队参数")
public class RPGTeamJoinParams extends RPGTeamMemberBaseParams {

    /** 车队序列号 */
    @NotEmpty(message = "车队序列号不能为空")
    @ApiModelProperty(value = "车队序列号", required = true, position = 1, example = "225298448138764288")
    private String sequence;

    public RPGTeamJoinParams() {
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

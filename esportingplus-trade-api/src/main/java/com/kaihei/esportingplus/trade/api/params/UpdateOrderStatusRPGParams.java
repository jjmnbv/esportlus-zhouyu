package com.kaihei.esportingplus.trade.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

@Validated
@ApiModel(value = "更新RPG订单状态参数", description = "更新RPG订单状态参数")
public final class UpdateOrderStatusRPGParams implements Serializable {

    private static final long serialVersionUID = 8473106123150916777L;
    @NotBlank(message = "车队sequenceId不能为空")
    @ApiModelProperty(value = "车队sequenceId", required = true, position = 1, example = "223867704556126208")
    private String teamSequence;

    @NotNull(message = "车队状态不能为空")
    @Range(min = 0,max = 3,message = "车队状态有误，要求：0-3")
    @ApiModelProperty(value = "车队状态，0：准备中 1：已发车 2：已解散 3：已结束", required = true, position = 2, example = "0")
    private Integer teamStatus;

    @NotNull(message = "游戏结果")
    @Range(min = -1,max = 2,message = "游戏结果，要求：-1 到 2")
    @ApiModelProperty(value = "游戏结果，0:失败 1胜利 2:未打 -1：未知", required = true, position = 4, example = "2")
    private Integer gameResult;

    @NotEmpty(message = "车队队员列表不能为空")
    @ApiModelProperty(value = "车队队员列表", required = true, position = 5)
    private List<OrderTeamRPGMember> teamMembers;

    public String getTeamSequence() {
        return teamSequence;
    }

    public void setTeamSequence(String teamSequence) {
        this.teamSequence = teamSequence;
    }

    public Integer getTeamStatus() {
        return teamStatus;
    }

    public void setTeamStatus(Integer teamStatus) {
        this.teamStatus = teamStatus;
    }

    public List<OrderTeamRPGMember> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(
            List<OrderTeamRPGMember> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public Integer getGameResult() {
        return gameResult;
    }

    public void setGameResult(Integer gameResult) {
        this.gameResult = gameResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UpdateOrderStatusRPGParams that = (UpdateOrderStatusRPGParams) o;
        return Objects.equals(teamSequence, that.teamSequence) &&
                Objects.equals(teamStatus, that.teamStatus) &&
                Objects.equals(gameResult, that.gameResult) &&
                CollectionUtils.isEqualCollection(teamMembers, that.teamMembers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamSequence, teamStatus, gameResult, teamMembers);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

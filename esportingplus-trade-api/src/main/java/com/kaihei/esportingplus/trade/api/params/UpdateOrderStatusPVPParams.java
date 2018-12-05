package com.kaihei.esportingplus.trade.api.params;

import com.kaihei.esportingplus.common.enums.SettlementTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

@Validated
@ApiModel(value = "更新PVP订单状态参数", description = "更新PVP订单状态参数")
public final class UpdateOrderStatusPVPParams implements Serializable {


    private static final long serialVersionUID = 8076007986551164781L;
    @NotBlank(message = "车队sequenceId不能为空")
    @ApiModelProperty(value = "车队sequenceId", required = true, position = 1, example = "223867704556126208")
    private String teamSequence;

    @NotNull(message = "车队状态不能为空")
    @Range(min = 0,max = 3,message = "车队状态有误，要求：0-3")
    @ApiModelProperty(value = "车队状态，0：准备中 1：已发车 2：已解散 3：已结束", required = true, position = 2, example = "0")
    private Integer teamStatus;

    @NotNull(message = "结算局数/小时数不能为空")
    @ApiModelProperty(value = "结算局数/小时数，上分车队为局数，陪玩车队为小时数", required = true, position = 3, example = "0")
    private Integer  settleCounts;

    @NotNull(message = "结算模式不能为空")
    @ApiModelProperty(value = "结算模式: 1=局，2=小时", required = true, position = 3, example = "0")
    private SettlementTypeEnum settlementTypeEnum;

    @NotEmpty(message = "游戏结果不能为空")
    @ApiModelProperty(value = "游戏结果", required = true, position = 4)
    private List<GameResultPVPParams> gameResults;

    @NotEmpty(message = "车队队员列表不能为空")
    @ApiModelProperty(value = "车队队员列表", required = true, position = 5)
    private List<OrderTeamPVPMember> teamMembers;

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

    public List<OrderTeamPVPMember> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(
            List<OrderTeamPVPMember> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public List<GameResultPVPParams> getGameResults() {
        return gameResults;
    }

    public void setGameResults(
            List<GameResultPVPParams> gameResults) {
        this.gameResults = gameResults;
    }

    public Integer getSettleCounts() {
        return settleCounts;
    }

    public void setSettleCounts(Integer settleCounts) {
        this.settleCounts = settleCounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UpdateOrderStatusPVPParams that = (UpdateOrderStatusPVPParams) o;
        return Objects.equals(teamSequence, that.teamSequence) &&
                Objects.equals(teamStatus, that.teamStatus) &&
                Objects.equals(settleCounts, that.settleCounts) &&
                settlementTypeEnum == that.settlementTypeEnum &&
                Objects.equals(gameResults, that.gameResults) &&
                Objects.equals(teamMembers, that.teamMembers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamSequence, teamStatus, settleCounts, settlementTypeEnum, gameResults,
                teamMembers);
    }

    public SettlementTypeEnum getSettlementTypeEnum() {
        return settlementTypeEnum;
    }

    public void setSettlementTypeEnum(
            SettlementTypeEnum settlementTypeEnum) {
        this.settlementTypeEnum = settlementTypeEnum;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}

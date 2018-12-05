/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.kaihei.esportingplus.trade.api.params;

import com.kaihei.esportingplus.common.enums.SettlementTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

@Validated
@ApiModel(value = "结算暴鸡PVP收益参数", description = "结算暴鸡PVP收益参数")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class PVPInComeParams implements Serializable {

    private static final long serialVersionUID = 1306519309162324645L;
    @NotNull(message = "游戏ID不能为空")
    @ApiModelProperty(value = "游戏ID", required = true)
    private Integer gameId;

    @NotBlank(message = "车队sequenceId不能为空")
    @ApiModelProperty(value = "车队sequenceId", required = true)
    private String teamSequence;

    @NotNull(message = "结算类型不能为空")
    @ApiModelProperty(value = "结算类型", required = true)
    private SettlementTypeEnum settlementTypeEnum;

    @NotNull(message = "预计局数不能为空")
    @ApiModelProperty(value = "预计打多少局(上分/陪玩)", required = true)
    private Integer preRounds;

    @NotBlank(message = "当前用户UID不能为空")
    @ApiModelProperty(value = "当前用户UID", required = true)
    private String uid;

    @NotNull(message = "当前队员身份不能为空")
    @ApiModelProperty(value = "当前队员身份，参考UserIdentityEnum", required = true)
    private Integer userIdentity;

    @NotNull(message = "当前暴鸡等级不能为空")
    @ApiModelProperty(value = "当前暴鸡等级", required = true)
    private Integer baojiLevel;

    @NotEmpty(message = "车队队员列表不能为空")
    @ApiModelProperty(value = "车队队员列表", required = true)
    @Valid
    private List<OrderTeamPVPMember> teamMembers;

    public String getTeamSequence() {
        return teamSequence;
    }

    public void setTeamSequence(String teamSequence) {
        this.teamSequence = teamSequence;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<OrderTeamPVPMember> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(
            List<OrderTeamPVPMember> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Integer getUserIdentity() {
        return userIdentity;
    }

    public Integer getBaojiLevel() {
        return baojiLevel;
    }

    public void setBaojiLevel(Integer baojiLevel) {
        this.baojiLevel = baojiLevel;
    }

    public void setUserIdentity(Integer userIdentity) {
        this.userIdentity = userIdentity;
    }

    public SettlementTypeEnum getSettlementTypeEnum() {
        return settlementTypeEnum;
    }

    public void setSettlementTypeEnum(
            SettlementTypeEnum settlementTypeEnum) {
        this.settlementTypeEnum = settlementTypeEnum;
    }

    public Integer getPreRounds() {
        return preRounds;
    }

    public void setPreRounds(Integer preRounds) {
        this.preRounds = preRounds;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

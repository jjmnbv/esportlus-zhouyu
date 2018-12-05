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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

@Validated
@ApiModel(value = "结算暴鸡RPG收益参数", description = "结算暴鸡RPG收益参数")
public final class RPGInComeParams implements Serializable {

    private static final long serialVersionUID = 5227444608515981152L;

    @ApiModelProperty(value = "游戏code", required = true, position = 1, example = "88")
    private Integer gameCode;

    @ApiModelProperty(value = "副本code", required = true, position = 2, example = "2")
    private Integer raidCode;

    @NotEmpty(message = "车队sequenceId不能为空")
    @ApiModelProperty(value = "车队sequenceId", required = true, position = 3, example = "223867704556126208")
    private String teamSequence;

    @NotNull(message = "车队金额")
    @ApiModelProperty(value = "车队金额", required = true, position = 4, example = "0")
    private Integer teamPrice;

    @NotEmpty(message = "用户UID不能为空")
    @ApiModelProperty(value = "用户UID", required = true, position = 5, example = "12314")
    private String uid;

    @NotEmpty(message = "队员身份不能为空")
    @ApiModelProperty(value = "队员身份，0：老板，1：暴鸡，2：暴娘，9：队长", required = true, position = 6, example = "0")
    private Integer userIdentity;

    @NotEmpty(message = "车队队员列表不能为空")
    @ApiModelProperty(value = "车队队员列表", required = true, position = 3)
    private List<OrderTeamRPGMember> teamMembers;

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

    public List<OrderTeamRPGMember> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(
            List<OrderTeamRPGMember> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public Integer getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(Integer userIdentity) {
        this.userIdentity = userIdentity;
    }

    public Integer getTeamPrice() {
        return teamPrice;
    }

    public void setTeamPrice(Integer teamPrice) {
        this.teamPrice = teamPrice;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

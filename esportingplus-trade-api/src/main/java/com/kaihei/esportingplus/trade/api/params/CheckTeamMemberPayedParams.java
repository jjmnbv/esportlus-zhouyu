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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Range;

@ApiModel(value = "校验队员(老板)是否已支付参数", description = "校验队员(老板)是否已支付参数")
public final class CheckTeamMemberPayedParams implements Serializable {

    private static final long serialVersionUID = -8596148858175130880L;

    @NotEmpty(message = "车队sequenceId不能为空")
    @ApiModelProperty(value = "车队sequenceId", required = true, position = 1, example = "223867704556126208")
    private String teamSequeue;

    @NotBlank(message = "用户uid不能为空")
    @ApiModelProperty(value = "用户uid", required = true, position = 2, example = "2")
    private String uid;

    @NotEmpty(message = "车队队员身份不能为空")
    @Range(min = 0, max = 9, message = "车队队员身份有误,要求:0-3")
    @ApiModelProperty(value = "车队队员身份，0：老板，1：暴鸡，2：暴娘，3：队长", required = false, position = 3, example = "0")
    private Integer userIdentity;

    @ApiModelProperty(value = "订单状态，0：准备中(暴鸡队员）, "
            + "1：待付款（老板上车后的状态），"
            + "2：已付款（老板付款后的状态），"
            + "3：已取消，4：已完成",
            required = false, position = 3, example = "0")
    private Integer payStatus;

    public String getTeamSequeue() {
        return teamSequeue;
    }

    public void setTeamSequeue(String teamSequeue) {
        this.teamSequeue = teamSequeue;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(Integer userIdentity) {
        this.userIdentity = userIdentity;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

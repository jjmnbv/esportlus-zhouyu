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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

@ApiModel(value = "订单查询参数", description = "订单查询参数")
@Validated
public final class OrderQueryParams implements Serializable {

    private static final long serialVersionUID = -982575039646826727L;
    @NotBlank(message = "车队sequenceId不能为空")
    @ApiModelProperty(value = "车队sequenceId", required = true, position = 1, example = "223867704556126208")
    private String teamSequence;

    @ApiModelProperty(value = "优惠券id(可选)", required = false, position = 2, example = "[0]")
    private List<Long> couponId;

    public String getTeamSequence() {
        return teamSequence;
    }

    public void setTeamSequence(String teamSequence) {
        this.teamSequence = teamSequence;
    }

    public List<Long> getCouponId() {
        return couponId;
    }

    public void setCouponId(List<Long> couponId) {
        this.couponId = couponId;
    }
}

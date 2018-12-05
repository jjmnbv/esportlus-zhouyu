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

@ApiModel(value = "校验角色是否已接单参数", description = "校验角色是否已接单参数")
public final class CheckGameRoleInOrderParams implements Serializable {

    private static final long serialVersionUID = -917831560615928898L;

    @NotBlank(message = "用户UID不能为空")
    @ApiModelProperty(value = "用户UID", required = true, position = 1, example = "abcdefg")
    private String uid;

    @NotEmpty(message = "游戏code不能为空")
    @ApiModelProperty(value = "游戏code", required = true, position = 2, example = "1")
    private Integer gameCode;

    @NotEmpty(message = "游戏角色ID不能为空")
    @ApiModelProperty(value = "游戏角色ID", required = true, position = 3, example = "1")
    private Long roleId;

    @NotEmpty(message = "副本code不能为空")
    @ApiModelProperty(value = "副本code", required = true, position = 4, example = "0")
    private Integer raidCode;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getGameCode() {
        return gameCode;
    }

    public void setGameCode(Integer gameCode) {
        this.gameCode = gameCode;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Integer getRaidCode() {
        return raidCode;
    }

    public void setRaidCode(Integer raidCode) {
        this.raidCode = raidCode;
    }
}

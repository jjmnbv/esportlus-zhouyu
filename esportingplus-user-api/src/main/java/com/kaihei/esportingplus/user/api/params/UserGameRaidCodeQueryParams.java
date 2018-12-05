package com.kaihei.esportingplus.user.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel(value = "可选参数对象", description = "用户角色查询可选参数")
public class UserGameRaidCodeQueryParams extends UserGameQueryBaseParams {
    /**
     * 副本code
     */
    @ApiModelProperty(value = "副本code", required = false, position = 1, example = "")
    private Integer raidCode;

    public Integer getRaidCode() {
        return raidCode;
    }

    public void setRaidCode(Integer raidCode) {
        this.raidCode = raidCode;
    }
}

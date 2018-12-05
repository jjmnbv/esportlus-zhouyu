package com.kaihei.esportingplus.user.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(value = "可选参数对象", description = "用户角色查询可选参数")
public class UserGameQueryBaseParams {

    /**
     * 角色Id
     */
    @ApiModelProperty(value = "角色ID", required = false, position = 1, example = "")
    private Long roleId;
    /**
     * 服务大区code
     */
    @ApiModelProperty(value = "服务大区code", required = false, position = 1, example = "")
    private Integer zoneBigCode;
    /**
     * 服务小区code
     */
    @ApiModelProperty(value = "服务小区code", required = false, position = 1, example = "")
    private Integer zoneSmallCode;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Integer getZoneBigCode() {
        return zoneBigCode;
    }

    public void setZoneBigCode(Integer zoneBigCode) {
        this.zoneBigCode = zoneBigCode;
    }

    public Integer getZoneSmallCode() {
        return zoneSmallCode;
    }

    public void setZoneSmallCode(Integer zoneSmallCode) {
        this.zoneSmallCode = zoneSmallCode;
    }
}

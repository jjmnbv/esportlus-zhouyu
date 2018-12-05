package com.kaihei.esportingplus.gamingteam.api.params;

import com.kaihei.esportingplus.common.paging.PagingRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@ApiModel("车队查询列表请求参数")
public class TeamQueryParams extends PagingRequest {

    /**
     * 副本Code
     */
    @ApiModelProperty(value = "副本code", required = false, position = 1, example = "")
    private Integer raidCode;
    /**
     * 服务器大区Code，如果服务器小区code不为空，以服务器小区为准
     */
    @ApiModelProperty(value = "大区code", required = false, position = 1, example = "")
    private Integer zoneBigCode;
    /**
     * 服务器小区Code
     */
    @ApiModelProperty(value = "小区code", required = false, position = 1, example = "")
    private Integer zoneSmallCode;

    public Integer getRaidCode() {
        return raidCode;
    }

    public void setRaidCode(Integer raidCode) {
        this.raidCode = raidCode;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

package com.kaihei.esportingplus.riskrating.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

@Validated
@ApiModel(value = "设备白名单查询", description = "设备白名单查询")
public class RiskDeviceWhiteQueryParams implements Serializable{

    private static final long serialVersionUID = -1505151834952943324L;
    @ApiModelProperty(value = "设备号", required = false, example = "9c18f861")
    private String deviceNo;
    @ApiModelProperty(value = "查询开始时间", required = false, example = "2018-10-10 10:00:00")
    private Date startDate;
    @ApiModelProperty(value = " 查询结束时间", required = false, example = "2018-12-10 10:00:00")
    private Date endDate;
    @NotNull(message = "当前页不能为空")
    private Integer offset;
    @NotNull(message = "每页多少行不能为空")
    private Integer limit;

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

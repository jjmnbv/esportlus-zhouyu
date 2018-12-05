package com.kaihei.esportingplus.payment.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: zhouyu
 * @Date: 2018/10/11 16:59
 * @Description:
 */
@Validated
@ApiModel(value = "工作室订单收益查询", description = "工作室订单收益查询参数")
public class WorkRoomQueryParams implements Serializable {

    @NotBlank
    @ApiModelProperty(value = "开始时间（时间戳,单位毫秒）", required = true, example = "22384556126208")
    private String beginTime;

    @NotBlank
    @ApiModelProperty(value = "结束时间", required = true, example = "22386556126208")
    private String endTime;

    @NotBlank
    @ApiModelProperty(value = "用户id", required = true, example = "2eDSF12w8")
    private String userId;

    @NotBlank
    @ApiModelProperty(value = "订单类型", required = true, example = "20")
    private List<Integer> orderTypes;

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Integer> getOrderTypes() {
        return orderTypes;
    }

    public void setOrderTypes(List<Integer> orderTypes) {
        this.orderTypes = orderTypes;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

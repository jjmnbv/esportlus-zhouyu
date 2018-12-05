package com.kaihei.esportingplus.payment.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Author: zhouyu
 * @Date: 2018/10/9 15:42
 * @Description:
 */

@ApiModel(value = "查询结算收益分成参数", description = "python 入参参数")
public class OrderQueryParam implements Serializable {

    @NotBlank(message = "订单ID不能为空")
    @ApiModelProperty(value = "订单id", required = true, example = "20158516545445")
    private String orderId;

    @ApiModelProperty(value = "订单类型 0滴滴单 1车队单", required = true, example = "0")
    private int orderType;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

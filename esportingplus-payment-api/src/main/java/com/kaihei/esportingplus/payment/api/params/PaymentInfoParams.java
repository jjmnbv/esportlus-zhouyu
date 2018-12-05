package com.kaihei.esportingplus.payment.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 获取充值订单详情参数
 *
 * @author: xusisi
 **/
@Validated
@ApiModel(value = "获取充值订单详情参数", description = "获取充值订单详情参数")
public class PaymentInfoParams implements Serializable {

    private static final long serialVersionUID = -764751855495238297L;

    @NotBlank(message = "用户ID不能为空")
    @ApiModelProperty(value = "用户ID", required = true, example = "13242343240")
    private String userId;

    @NotBlank(message = "订单ID不能为空")
    @ApiModelProperty(value = "订单ID", required = true, example = "1232312321")
    private String orderId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

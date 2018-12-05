package com.kaihei.esportingplus.payment.api.params;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

/**
 * @ClassName ClosePayOrderParams
 * @Description 关闭支付订单、取消支付订单入参
 * @Author xusisi
 * @Date 2018/11/26 下午2:44
 */
@Validated
public class CloseOrCancelPayOrderParams {

    @ApiModelProperty(name = "orderType 业务订单类型", required = false, example = "12")
    private String orderType;

    @ApiModelProperty(name = "outTradeNo 业务订单号", required = false, example = "10010")
    private String outTradeNo;

    @ApiModelProperty(name = "payOrderId 支付订单号", required = false, example = "10000010")
    private String payOrderId;

    @ApiModelProperty(name = "userId 用户Id", required = false, example = "100100")
    private String userId;

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

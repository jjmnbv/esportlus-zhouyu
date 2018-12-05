package com.kaihei.esportingplus.payment.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * 更新暴鸡币支付入参
 *
 * @author: xusisi
 **/
@Validated
@ApiModel(value = "更新暴鸡币支付订单参数", description = "更新暴鸡币支付订单参数")
public class GCoinPaymentUpdateParams {

    /***
     * 用户ID
     */
    @NotBlank(message = "用户ID userId不能为空")
    @ApiModelProperty(value = "用户ID userId", required = true, example = "20180820163345228956821526941696")
    private String userId;

    /***
     *业务订单号
     */
    @NotBlank(message = "业务订单号outTradeNo不能为空")
    @ApiModelProperty(value = "业务订单号outTradeNo", required = true, example = "123454664")
    private String outTradeNo;

    /***
     * 业务订单类型
     */
    @NotBlank(message = "业务订单类型orderType不能为空")
    @ApiModelProperty(value = "业务订单类型orderType", required = true, example = "001")
    private String orderType;

    /***
     * 支付订单号
     */
    @NotBlank(message = "支付订单号orderId不能为空")
    @ApiModelProperty(value = "支付订单号orderId", required = true, example = "123123123123")
    private String orderId;

    /***
     * 回调URL
     */
    @NotBlank(message = "回调URL不能为空")
    @ApiModelProperty(value = "回调URL notifyUrl", required = true, example = "http://www.baidu.com")
    private String notifyUrl;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

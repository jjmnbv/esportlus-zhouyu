package com.kaihei.esportingplus.payment.api.params;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 结算收益分成参数
 *
 * @author: zhouyu
 */
@ApiModel(value = "结算收益分成参数", description = "python 入参参数")
public class OrderIncomeParams implements Serializable {

    private static final long serialVersionUID = -2129068021418209819L;

    @NotBlank(message = "暴鸡、暴娘ID不能为空")
    @ApiModelProperty(value = "暴鸡、暴娘id", required = true, example = "20158516545445")
    private String userId;

    @NotBlank(message = "订单ID不能为空")
    @ApiModelProperty(value = "订单id", required = true, example = "20158516545445")
    private String orderId;

    @ApiModelProperty(value = "订单类型 0滴滴单 1车队单", required = true, example = "0")
    private int orderType;

    @ApiModelProperty(value = "附加数据", required = true)
    private String attach;

    @ApiModelProperty(value = "订单金额，单位分", required = true, example = "1000")
    private int amount;

    @NotBlank(message = "异步回调地址不能为空")
    @ApiModelProperty(value = "异步回调地址", required = true)
    private String notifyUrl;

    @NotBlank(message = "数据来源不能为空")
    @ApiModelProperty(value = "数据来源", required = true)
    private String sourceId;

    @JsonIgnore
    private String body = "暴鸡暴娘和平台按照抽成比例进行暴击值抽成";

    @JsonIgnore
    private String subject = "订单结算暴击值抽成";

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

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

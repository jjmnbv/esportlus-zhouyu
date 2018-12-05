package com.kaihei.esportingplus.payment.api.params;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
public class RefundOrderParams {

    /***
     * 本次需要退款的金额，该金额不能大于订单金额,单位为分
     */
    @NotBlank(message = "退款金额 refundAmount 不能为空")
    @ApiModelProperty(name = "退款金额 refundAmount", required = true, example = "1000")
    private String refundAmount;

    /***
     * 业务订单号
     */
    @NotBlank(message = "业务订单号 outTradeNo 不能为空")
    @ApiModelProperty(name = "业务订单号 outTradeNo ", required = true, example = "10010")
    private String outTradeNo;

    /***
     * 业务订单类型
     */
    @NotBlank(message = "业务订单来下 orderType 不能为空")
    @ApiModelProperty(name = "业务订单来下 orderType ", required = true, example = "17")
    private String orderType;

    /***
     * 用户ID
     */
    @NotBlank(message = "用户id userId 不能为空")
    @ApiModelProperty(name = "用户ID userId ", required = true, example = "10010")
    private String userId;

    /**
     * 原支付订单号
     */
    @ApiModelProperty(name = "原支付订单号 payOrderId ", required = true, example = "10010")
    private String payOrderId;

    /***
     * 业务退款号，本次退款唯一的流水号
     */
    @NotBlank(message = "业务退款订单号 outRefundNo不能为空")
    @ApiModelProperty(name = "业务退款订单号 outRefundNo", required = true, example = "10010")
    private String outRefundNo;

    /***
     * 退款通知回调URL
     */
    @NotBlank(message = "退款回调地址 notifyUrl不能为空")
    @ApiModelProperty(name = "退款回调地址 notifyUrl", required = true, example = "http://www.aaa.com")
    private String notifyUrl;

    /***
     * 主题
     */
    @NotBlank(message = "主题 subject 不能为空")
    @ApiModelProperty(value = "主题 subject", required = true, example = "支付宝支付")
    private String subject;

    /***
     * 内容
     */
    @ApiModelProperty(value = "内容 body", required = false, example = "支付宝支付-3元")
    private String body;

    /***
     * 描述
     */
    @ApiModelProperty(value = "描述 description", required = false, example = "消息描述信息")
    private String description;

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

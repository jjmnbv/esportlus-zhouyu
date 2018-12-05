package com.kaihei.esportingplus.payment.api.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

/**
 * 暴鸡币充值订单详情
 *
 * @author: xusisi
 **/
public class GCoinRechargeVo {
    /***
     * 充值订单ID
     */
    private String orderId;

    /***
     * 业务订单类型
     */
    private String orderType;

    /***
     * 业务订单号
     */
    private String outTradeNo;

    /***
     * 主题
     */
    private String subject;

    /***
     * 内容
     */
    private String body;

    /***
     * 描述
     */
    private String description;

    /***
     * 用户ID
     */
    private String userId;

    /***
     * 充值金额（单位分）
     */
    private BigDecimal amount;

    /***
     * 暴鸡币数量
     */
    private BigDecimal gcoinAmount;

    /***
     * 支付渠道
     * C001 暴鸡币钱包支付
     * C002 QQ支付
     * C003 微信支付
     * C004 支付宝支付
     * C005 APPLE 支付
     * C006 平台系统
     */
    private String channel;

    /***
     * 操作系统
     * PC表示PC
     * H5表示h5
     * ANDROID表示android
     * IOS表示ios
     * MP表示小程序
     * PA表示微信公众号
     */
    private String sourceId;

    /**
     * 第三方支付时间，付款时间
     */
    private String paymentDate;

    /***
     * 第三方支付订单号
     */
    private String paymentOrderNo;


    /***
     * 订单状态
     * 000 支付失败，超时未支付
     * 001 待支付
     * 002 支付成功，业务订单待结算
     * 003 支付成功，业务订单已完成
     * 004 支付成功，退款中
     * 005 已退款
     * 006 已结清，不可再退款
     */
    private String state;

    /**
     * apple设备ID
     */
    private String deviceId;

    /**
     * 货币类型
     */
    private String currencyType;

    /**
     * 充值类型
     */
    private String rechargeType;

    /***
     * 备注
     */
    private String remarks;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getGcoinAmount() {
        return gcoinAmount;
    }

    public void setGcoinAmount(BigDecimal gcoinAmount) {
        this.gcoinAmount = gcoinAmount;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentOrderNo() {
        return paymentOrderNo;
    }

    public void setPaymentOrderNo(String paymentOrderNo) {
        this.paymentOrderNo = paymentOrderNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(String rechargeType) {
        this.rechargeType = rechargeType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

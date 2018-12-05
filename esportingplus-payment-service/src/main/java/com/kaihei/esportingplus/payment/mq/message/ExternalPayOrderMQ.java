package com.kaihei.esportingplus.payment.mq.message;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 第三方支付订单MQ信息
 *
 * @author xusisi
 **/
public class ExternalPayOrderMQ {

    /***
     * 支付订单ID
     */
    private String orderId;

    /***
     * 订单状态
     */
    private String orderState;

    /***
     * 支付配置信息
     */
    private String appId;

    /***
     * channelTag
     */
    private String channelTag;

    /***
     * 支付渠道
     * 微信、支付宝、云账户
     * Alipay,wechatpay,cloudAccount
     */
    private String channelType;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getChannelTag() {
        return channelTag;
    }

    public void setChannelTag(String channelTag) {
        this.channelTag = channelTag;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

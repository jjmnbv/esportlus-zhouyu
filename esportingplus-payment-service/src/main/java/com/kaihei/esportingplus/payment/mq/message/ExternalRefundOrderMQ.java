package com.kaihei.esportingplus.payment.mq.message;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 第三方支付订单MQ信息
 *
 * @author xusisi
 **/
public class ExternalRefundOrderMQ {

    /***
     * 退款订单ID
     */
    private String orderId;

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
     */
    private String channelType;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getChannelTag() {
        return channelTag;
    }

    public void setChannelTag(String channelTag) {
        this.channelTag = channelTag;
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

package com.kaihei.esportingplus.payment.mq.message;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * MQ订单简要信息
 *
 * @author xusisi
 **/
public class MQSimpleOrder {

    private String orderId;

    private String notifyUrl;

    /***
     * 业务订单号
     */
    private String outTradeNo;

    private String userId;

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

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
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

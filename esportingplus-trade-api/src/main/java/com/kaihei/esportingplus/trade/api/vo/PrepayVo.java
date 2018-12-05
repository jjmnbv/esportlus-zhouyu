package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;

public class PrepayVo implements Serializable{

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 预支付金额：分
     */
    private int fee;

    /**
     * 支付回调地址
     */
    private String payNofityUrl;

    /**
     * 退款回调地址
     */
    private String refundNofityUrl;

    /**
     * 确认支付回调通知接口
     */
    private String payConfirmNofityUrl;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getPayNofityUrl() {
        return payNofityUrl;
    }

    public void setPayNofityUrl(String payNofityUrl) {
        this.payNofityUrl = payNofityUrl;
    }

    public String getRefundNofityUrl() {
        return refundNofityUrl;
    }

    public void setRefundNofityUrl(String refundNofityUrl) {
        this.refundNofityUrl = refundNofityUrl;
    }

    public String getPayConfirmNofityUrl() {
        return payConfirmNofityUrl;
    }

    public void setPayConfirmNofityUrl(String payConfirmNofityUrl) {
        this.payConfirmNofityUrl = payConfirmNofityUrl;
    }
}

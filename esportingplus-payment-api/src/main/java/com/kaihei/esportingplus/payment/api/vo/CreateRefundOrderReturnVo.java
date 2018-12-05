package com.kaihei.esportingplus.payment.api.vo;

import java.io.Serializable;

/**
 * @Author: xusisi
 * @Date: 2018/10/30 14:22
 * @Description:创建退款订单返回参数
 */
public class CreateRefundOrderReturnVo implements Serializable {

    /***
     * 支付订单号
     */
    private String payOrderId;

    /***
     * 业务订单号
     */
    private String outTradeNo;

    /**
     * 业务订单类型
     */
    private String orderType;

    /***
     * 业务退款订单号
     */
    private String outRefundNo;

    /**
     * 退款订单号
     */
    private String orderId;

    private String refundAmount;

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
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
}

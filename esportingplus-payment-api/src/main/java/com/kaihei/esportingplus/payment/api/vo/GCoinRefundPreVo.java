package com.kaihei.esportingplus.payment.api.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 暴鸡币退款订单详情
 *
 * @author: xusisi
 **/
public class GCoinRefundPreVo {

    /****
     * 业务订单号
     */
    private String outTradeNo;

    /**
     * 业务退款订单号
     */
    private String outRefundNo;

    /****
     * 退款订单号
     */
    private String orderId;

    /****
     * 业务订单类型
     */
    private Integer orderType;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

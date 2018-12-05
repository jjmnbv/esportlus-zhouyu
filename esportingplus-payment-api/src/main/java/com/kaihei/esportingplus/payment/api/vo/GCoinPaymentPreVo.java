package com.kaihei.esportingplus.payment.api.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 暴鸡币支付订单预支付信息
 *
 * @author: xusisi
 **/
public class GCoinPaymentPreVo {

    /***
     *业务订单ID
     */
    private String outTradeNo;

    /***
     * 业务订单类型
     */
    private Integer orderType;

    /***
     *  支付订单号
     */
    private String orderId;


    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

package com.kaihei.esportingplus.payment.api.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 暴鸡币预充值订单
 *
 * @author: xusisi
 **/
public class GCoinRechargePreVo {

    private String orderId;

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

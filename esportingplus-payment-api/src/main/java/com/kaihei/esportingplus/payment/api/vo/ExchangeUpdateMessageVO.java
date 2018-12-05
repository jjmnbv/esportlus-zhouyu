package com.kaihei.esportingplus.payment.api.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 暴击值兑换暴鸡币,Message
 *
 * @author: chenzhenjun
 **/
public class ExchangeUpdateMessageVO implements Serializable {

    private static final long serialVersionUID = -1136438890892507297L;

    /**
     * 提现订单ID
     */
    private String orderId;

    private String userId;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

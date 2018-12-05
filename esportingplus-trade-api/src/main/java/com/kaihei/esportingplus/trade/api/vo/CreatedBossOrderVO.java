package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;
import lombok.Data;

@Data
public class CreatedBossOrderVO implements Serializable {


    private static final long serialVersionUID = -6532374288558880843L;
    /**
     * 订单号
     */
    private String orderId;

    /**
     * 订单类型
     */
    private Integer orderType;

    /**
     * 支付金额（单位：分）
     */
    private Integer fee;

    /**
     * 支付状态
     */
    private Integer status;

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

    public Integer getFee() {
        return fee;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }

    @Override
    public String toString() {
        return "CreatedBossOrderVO{" +
                "orderId=" + orderId +
                ", orderType=" + orderType +
                ", fee=" + fee +
                '}';
    }
}

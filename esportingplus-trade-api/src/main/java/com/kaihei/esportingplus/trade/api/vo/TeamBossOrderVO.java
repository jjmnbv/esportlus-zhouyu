package com.kaihei.esportingplus.trade.api.vo;

import lombok.Data;

@Data
public class TeamBossOrderVO {

    /**
     * 订单ID
     */
    private Long id;

    /**
     * 用户预付金额，用户完成支付时产生的金额，单位为分
     */
    private Integer prepaidAmount;
    /**
     * 优惠金额，用户支付后使用优惠券或奖励金产生的折扣金额，单位为分，若无，记为0
     */
    private Integer discountAmount;
    /**
     * 订单序列号
     */
    private String sequeue;
    /**
     * 订单状态，0：准备中(暴鸡队员）, 1：待付款（老板上车后的状态），2：已付款（老板付款后的状态），3：已取消，4：已完成
     */
    private Integer status;

    public Long getId() {
        return id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPrepaidAmount() {
        return prepaidAmount;
    }

    public void setPrepaidAmount(Integer prepaidAmount) {
        this.prepaidAmount = prepaidAmount;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Integer discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getSequeue() {
        return sequeue;
    }

    public void setSequeue(String sequeue) {
        this.sequeue = sequeue;
    }
}

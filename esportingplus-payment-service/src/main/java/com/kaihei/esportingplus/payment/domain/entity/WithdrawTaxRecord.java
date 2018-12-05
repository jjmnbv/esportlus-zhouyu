package com.kaihei.esportingplus.payment.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 提现扣税记录实体类
 * @author chenzhenjun
 */
@Entity
public class WithdrawTaxRecord extends AbstractEntity{

    private static final long serialVersionUID = 6396480204577155679L;

    @Column(nullable = false, length = 50, columnDefinition = "varchar(50) COMMENT '订单号'")
    private String orderId;

    /**
     * 用户外键id
     */
    @Column(nullable = false, length = 10, columnDefinition = "varchar(10) COMMENT '用户外键id'")
    private String userId;

    /**
     * 税前金额
     */
    @Column(nullable = false, length = 10, columnDefinition = "int(10) COMMENT '税前金额(单位:分)'")
    private Integer totalFee;

    /**
     * 交税金额
     */
    @Column(nullable = false, length = 10, columnDefinition = "int(10) COMMENT '交税金额(单位:分)'")
    private Integer taxFee;

    /**
     * 税后金额
     */
    @Column(nullable = false, length = 10, columnDefinition = "int(10) COMMENT '税后金额(单位:分)'")
    private Integer incomeFee;

    /**
     * 扣款状态
     */
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16) COMMENT '状态(PROCESSING-处理中|SUCCESS-已完成|FAILED-失败|CANCEL-已取消)'")
    private String state;

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

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public Integer getTaxFee() {
        return taxFee;
    }

    public void setTaxFee(Integer taxFee) {
        this.taxFee = taxFee;
    }

    public Integer getIncomeFee() {
        return incomeFee;
    }

    public void setIncomeFee(Integer incomeFee) {
        this.incomeFee = incomeFee;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

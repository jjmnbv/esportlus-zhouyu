package com.kaihei.esportingplus.user.domain.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "payment_accountstatement")
public class PaymentAccountStatement {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 账单类型
     */
    @Column(name = "bill_type")
    private Integer billType;

    /**
     * 金额(单位: 分)
     */
    @Column(name = "total_fee")
    private Integer totalFee;

    /**
     * 订单号
     */
    @Column(name = "order_id")
    private String orderId;

    /**
     * 订单类型
     */
    @Column(name = "order_type")
    private String orderType;

    /**
     * 流水创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 用户
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 到账回执时间
     */
    @Column(name = "receipt_time")
    private Date receiptTime;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取账单类型
     *
     * @return bill_type - 账单类型
     */
    public Integer getBillType() {
        return billType;
    }

    /**
     * 设置账单类型
     *
     * @param billType 账单类型
     */
    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    /**
     * 获取金额(单位: 分)
     *
     * @return total_fee - 金额(单位: 分)
     */
    public Integer getTotalFee() {
        return totalFee;
    }

    /**
     * 设置金额(单位: 分)
     *
     * @param totalFee 金额(单位: 分)
     */
    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    /**
     * 获取订单号
     *
     * @return order_id - 订单号
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 设置订单号
     *
     * @param orderId 订单号
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    /**
     * 获取订单类型
     *
     * @return order_type - 订单类型
     */
    public String getOrderType() {
        return orderType;
    }

    /**
     * 设置订单类型
     *
     * @param orderType 订单类型
     */
    public void setOrderType(String orderType) {
        this.orderType = orderType == null ? null : orderType.trim();
    }

    /**
     * 获取流水创建时间
     *
     * @return create_time - 流水创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置流水创建时间
     *
     * @param createTime 流水创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取用户
     *
     * @return user_id - 用户
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户
     *
     * @param userId 用户
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取到账回执时间
     *
     * @return receipt_time - 到账回执时间
     */
    public Date getReceiptTime() {
        return receiptTime;
    }

    /**
     * 设置到账回执时间
     *
     * @param receiptTime 到账回执时间
     */
    public void setReceiptTime(Date receiptTime) {
        this.receiptTime = receiptTime;
    }
}
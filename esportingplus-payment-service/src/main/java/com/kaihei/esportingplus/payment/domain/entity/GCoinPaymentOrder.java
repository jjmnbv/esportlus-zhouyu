package com.kaihei.esportingplus.payment.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 暴鸡币支付订单表
 *
 * @author xusisi
 **/
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_order_id", columnNames = "orderId"),
        @UniqueConstraint(name = "uk_order_type_and_out_trade_no", columnNames = {"outTradeNo", "orderType"})},
        indexes = {
                @Index(name = "idx_order_type_and_out_trade_no", columnList = "outTradeNo,orderType"),
                @Index(name = "idx_user_id_and_order_id", columnList = "orderId,userId")
        })
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class GCoinPaymentOrder extends AbstractEntity {

    /***
     * 主题
     */
    @Column(length = 128, columnDefinition = "varchar(128) default '' COMMENT '主题'")
    private String subject;

    /***
     * 内容
     */
    @Column(length = 1024, columnDefinition = "varchar(1024) default '' COMMENT '内容'")
    private String body;

    /***
     * 描述
     */
    @Column(columnDefinition = "longtext COMMENT '描述'")
    private String description;

    /***
     * 操作终端
     *
     * PC PC端
     * ANDROID android
     * IOS ios
     * MP 小程序
     * H5 H5页面
     * PA 微信公众号
     * PLATFORM 暴鸡平台系统
     */
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16) COMMENT '操作系统(H5|IOS|MP|ANDROID|PA|PLATFORM)'")
    private String sourceId;

    /***
     * 支付金额（单位：元，保留两位小数）
     */
    @Column(nullable = false, length = 20, columnDefinition = "Decimal(19,2) default '0.00' COMMENT '支付金额（单位：元，保留两位小数）'")
    private BigDecimal amount;

    /****
     * 第三方业务订单号
     */
    @Column(length = 64, columnDefinition = "varchar(64) COMMENT '第三方业务订单号'")
    private String outTradeNo;

    /***
     * 用户ID
     */
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) COMMENT '用户ID'")
    private String userId;

    /****
     * 支付订单号
     */
    @Column(nullable = false, length = 128, columnDefinition = "varchar(128) COMMENT '支付订单号'")
    private String orderId;

    /****
     * 业务订单类型
     */
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16) COMMENT '业务订单类型'")
    private String orderType;

    /***
     * 附件数据（json字符串）
     */
    @Column(length = 128, columnDefinition = "varchar(128) COMMENT '附件数据（json字符串）'")
    private String attach;

    /***
     * 支付订单状态码
     *
     * 000 支付失败
     * 001 待支付
     * 002 支付成功，业务订单待结算
     * 003 支付成功，业务订单已完成
     * 004 支付成功，退款中
     * 005 已退款
     * 006 已结清，不可再退款
     */
    @Column(length = 16, nullable = false, columnDefinition = "varchar(16) COMMENT '支付订单状态码'")
    private String state;

    /***
     * 支付完成时间
     */
    @Column(length = 64, columnDefinition = "varchar(64) COMMENT '支付完成时间'")
    private String completedDate;

    /***
     * 已退金额（单位：元，保留两位小数）
     */
    @Column(length = 20, columnDefinition = "Decimal(19,2) default '0.00' COMMENT '已退金额（单位：元，保留两位小数）'")
    private BigDecimal refundedAmount;

    /***
     * 退款中的金额（单位：元，保留两位小数）
     */
    @Column(length = 20, columnDefinition = "Decimal(19,2) default '0.00' COMMENT '退款中的金额（单位：元，保留两位小数）'")
    private BigDecimal refundingAmount;

    /**
     * 账户余额(元）
     */
    @JsonIgnore
    @Transient
    private String balance;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public BigDecimal getRefundedAmount() {
        return refundedAmount;
    }

    public void setRefundedAmount(BigDecimal refundedAmount) {
        this.refundedAmount = refundedAmount;
    }

    public BigDecimal getRefundingAmount() {
        return refundingAmount;
    }

    public void setRefundingAmount(BigDecimal refundingAmount) {
        this.refundingAmount = refundingAmount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

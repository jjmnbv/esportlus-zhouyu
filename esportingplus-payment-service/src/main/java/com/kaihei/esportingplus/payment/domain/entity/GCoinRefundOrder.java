package com.kaihei.esportingplus.payment.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 暴鸡币退款订单表
 *
 * @author xusisi
 **/
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_order_id", columnNames = "orderId"),
        @UniqueConstraint(name = "uk_order_type_and_out_refund_no", columnNames = {"outRefundNo","orderType"})},
        indexes = {
                @Index(name = "idx_order_type_and_out_refund_no", columnList = "outRefundNo,orderType"),
                @Index(name = "idx_user_id_and_order_id", columnList = "orderId,userId")
        })
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GCoinRefundOrder extends AbstractEntity {

    private static final long serialVersionUID = -5995942326465825450L;
    /***
     * 主题
     */
    @Column(columnDefinition = "varchar(128) default ''  COMMENT '主题'")
    private String subject;

    /***
     * 内容
     */
    @Column(length = 1024, columnDefinition = "varchar(1024) default ''  COMMENT '内容'")
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
     * 退款金额（单位：元，保留两位小数）
     */
    @Column(nullable = false, columnDefinition = "Decimal(19,2) default '0.00' COMMENT '退款金额（单位：元，保留两位小数）'")
    private BigDecimal amount;

    /****
     * 业务订单号
     */
    @Column(columnDefinition = "varchar(64) COMMENT '业务订单号'")
    private String outTradeNo;

    /**
     * 业务退款订单号
     */
    @Column(columnDefinition = "varchar(64) COMMENT '业务退款订单号'")
    private String outRefundNo;

    /***
     * 用户ID
     */
    @Column(nullable = false, columnDefinition = "varchar(64) COMMENT '用户ID'")
    private String userId;

    /****
     * 退款订单号
     */
    @Column(nullable = false, length = 128, columnDefinition = "varchar(128) COMMENT '退款订单号'")
    private String orderId;

    /****
     * 业务订单类型
     */
    @Column(nullable = false, columnDefinition = "varchar(30) COMMENT '业务订单类型'")
    private String orderType;

    /***
     * 附件数据（json字符串）
     */
    @Column(columnDefinition = "varchar(128) COMMENT '附件数据（json字符串）'")
    private String attach;

    /***
     * 退款订单状态码
     *
     * 000 退款失败
     * 001 退款中
     * 002 已退款
     */
    @Column(length = 16, nullable = false, columnDefinition = "varchar(16) COMMENT '退款订单状态码'")
    private String state;

    /***
     * 退款完成时间
     */
    @Column(columnDefinition = "varchar(64) COMMENT '支付完成时间'")
    private String completedDate;

    /**
     * 账户余额(元）
     */
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

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
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

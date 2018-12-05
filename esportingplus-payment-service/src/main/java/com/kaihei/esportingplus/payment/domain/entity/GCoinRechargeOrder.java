package com.kaihei.esportingplus.payment.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 暴鸡币充值订单表
 *
 * @author xusisi
 * @create 2018-09-25 13:04
 **/
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_order_id", columnNames = "orderId")},
        indexes = {
                @Index(name = "idx_user_id_and_order_id", columnList = "orderId,userId ")
        })
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GCoinRechargeOrder extends AbstractEntity {

    /***
     * 业务订单号
     */
    @Column(length = 64, columnDefinition = "varchar(64) default '' COMMENT '业务订单号'")
    private String outTradeNo;

    /***
     * 业务订单类型
     */
    @Column(length = 30, columnDefinition = "varchar(30) default '' COMMENT '业务订单类型'")
    private String orderType;

    /***
     * 充值订单ID
     */
    @Column(nullable = false, length = 128, columnDefinition = "varchar(128) COMMENT '充值订单号'")
    private String orderId;
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
     * 用户ID
     */
    @Column(length = 64, nullable = false, columnDefinition = "varchar(64)  COMMENT '用户ID'")
    private String userId;

    /***
     * 充值金额（单位为元，两位小数）
     */
    @Column(nullable = false, columnDefinition = "Decimal(19,2) default '0.00' COMMENT '充值金额（单位为元，两位小数）'")
    private BigDecimal amount;

    /***
     * 暴鸡币数量
     */
    @Column(nullable = false, columnDefinition = "Decimal(19,2) default '0.00' COMMENT '暴鸡币数量'")
    private BigDecimal gcoinAmount;

    /***
     * 支付渠道
     *
     * C001 暴鸡币钱包支付
     * C002 QQ支付
     * C003 微信支付
     * C004 支付宝支付
     * C005 APPLE 支付
     * C006 平台系统
     * C007 云账户
     */
    @Column(length = 16, columnDefinition = "varchar(16) COMMENT '支付渠道'")
    private String channel;

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

    /**
     * 第三方支付时间，付款时间
     */
    @Column(length = 64, columnDefinition = "varchar(64) COMMENT '第三方支付时间，付款时间'")
    private String paymentDate;

    /***
     * 第三方支付订单号
     */
    @Column(length = 128, columnDefinition = "varchar(128) COMMENT '第三方支付订单号'")
    private String paymentOrderNo;

    /***
     * 订单状态
     *
     * 000 未付款交易超时关闭
     * 001 交易创建，等待买家付款
     * 002 支付后全额退款
     * 003 交易支付成功
     * 004 交易结束，不可退款
     */
    @Column(length = 16, nullable = false, columnDefinition = "varchar(16) COMMENT '订单状态'")
    private String state;

    /**
     * apple设备ID
     */
    @Column(length = 128, columnDefinition = "varchar(128) COMMENT 'apple设备ID'")
    private String deviceId;

    /**
     * 货币类型
     */
    @Column(length = 16, columnDefinition = "varchar(16) COMMENT '货币类型'")
    private String currencyType;

    /***
     * 实收金额
     */
    @Column(columnDefinition = "Decimal(19,2) default '0.00' COMMENT '实收金额'")
    private BigDecimal payInAmount;

    /***
     * 暴鸡币充值类型
     *
     * 000 前台用户主动充值
     * 001 投诉补偿
     * 002 订单补贴
     * 003 活动补贴
     * 004 消费奖励
     *
     */
    @Column(length = 16, columnDefinition = "varchar(16) COMMENT '充值类型'")
    private String rechargeType;

    /***
     * 备注
     */
    @Column(length = 255, columnDefinition = "varchar(255) COMMENT '备注'")
    private String remarks;

    /**
     * 账户余额(元）
     */
    @JsonIgnore
    @Transient
    private String balance;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getGcoinAmount() {
        return gcoinAmount;
    }

    public void setGcoinAmount(BigDecimal gcoinAmount) {
        this.gcoinAmount = gcoinAmount;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentOrderNo() {
        return paymentOrderNo;
    }

    public void setPaymentOrderNo(String paymentOrderNo) {
        this.paymentOrderNo = paymentOrderNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public BigDecimal getPayInAmount() {
        return payInAmount;
    }

    public void setPayInAmount(BigDecimal payInAmount) {
        this.payInAmount = payInAmount;
    }

    public String getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(String rechargeType) {
        this.rechargeType = rechargeType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
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

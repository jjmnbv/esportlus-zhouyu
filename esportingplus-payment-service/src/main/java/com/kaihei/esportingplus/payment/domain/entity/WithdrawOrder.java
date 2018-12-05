package com.kaihei.esportingplus.payment.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 提现/兑换 订单表
 *
 * @author: xusisi, chenzhenjun
 **/
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_order_id", columnNames = "orderId")},
        indexes = {@Index(name = "idx_out_trade_no", columnList = "outTradeNo"),
                @Index(name = "idx_order_id_and_user_id", columnList = "userId,orderId")
        })
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WithdrawOrder extends AbstractEntity {

    private static final long serialVersionUID = 3994560975334942150L;

    /***
     * 订单Id
     */
    @Column(nullable = false, columnDefinition = "varchar(128) COMMENT '订单id'")
    private String orderId;

    /**
     * 订单类型
     * <p>
     * 001 退款订单
     * 002 提现订单
     * 003 暴击值兑换订单
     * 004 暴鸡币打赏订单
     * 005 结算订单
     * 006 充值暴鸡币订单
     * 007 暴鸡币支付订单
     */
    @Column(nullable = false, columnDefinition = "varchar(32) COMMENT '订单类型(001|002)'")
    private String orderType;

    /***
     * 主题
     */
    @Column(columnDefinition = "varchar(128) default '' COMMENT '主题'")
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
    @Column(nullable = false, length = 128, columnDefinition = "varchar(128) COMMENT '用户ID'")
    private String userId;

    /***
     * 提现暴击值（单位为元，两位小数）
     */
    @Column(nullable = false, columnDefinition = "Decimal(19,2) default '0.00' COMMENT '提现暴击值'")
    private BigDecimal amount;

    /***
     * 暴击值数量
     */
    @Column(nullable = false, columnDefinition = "Decimal(19,2) default '0.00' COMMENT '提现暴击值'")
    private BigDecimal starAmount;

    /***
     * 手续费(预留字段)
     */
    @Column(columnDefinition = "Decimal(19,2) default '0.00' COMMENT '手续费'")
    private BigDecimal fee;

    /***
     * 提现订单状态
     *
     * 001（交易创建）
     * 002（提现交易超时关闭）
     * 003（提现交易成功）
     * 004（提交交易失败）
     */
    @Column(nullable = false, length = 10, columnDefinition = "varchar(10) COMMENT '订单状态'")
    private String state;

    /**
     * 支付渠道
     * <p>
     * C001 暴鸡币钱包支付 C002 QQ支付 C003 微信支付 C004 支付宝支付 C005 苹果支付 C006 平台系统支付 C007 云账户支付
     */
    @Column(nullable = false, length = 10, columnDefinition = "varchar(10) COMMENT '提现渠道'")
    private String channel;

    /**
     * 提现是否需要通知
     */
    @Column(nullable = false, columnDefinition = "varchar(1) COMMENT '提现是否需要通知(1-需要 0-不需)'")
    private String isNotify;

    /**
     * 第三方订单号(只有提现有)
     */
    @Column(columnDefinition = "varchar(64) COMMENT '第三方订单号'")
    private String outTradeNo;

    /**
     * 货币类型
     */
    @Column(nullable = false, length = 20, columnDefinition = "varchar(20) COMMENT '货币类型(starlight默认)'")
    private String moneyType;

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
     * 账户余额(元）
     */
    @Transient
    private String balance;

    /**
     * 暴击值账户余额(元）
     */
    @Transient
    private String gCoinBalance;

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

    public BigDecimal getStarAmount() {
        return starAmount;
    }

    public void setStarAmount(BigDecimal starAmount) {
        this.starAmount = starAmount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getIsNotify() {
        return isNotify;
    }

    public void setIsNotify(String isNotify) {
        this.isNotify = isNotify;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getgCoinBalance() {
        return gCoinBalance;
    }

    public void setgCoinBalance(String gCoinBalance) {
        this.gCoinBalance = gCoinBalance;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
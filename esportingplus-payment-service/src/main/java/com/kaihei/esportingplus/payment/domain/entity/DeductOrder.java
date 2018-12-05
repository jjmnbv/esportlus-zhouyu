package com.kaihei.esportingplus.payment.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 后台扣款订单
 *
 * @author xusisi
 * @create 2018-10-09 16:03
 **/
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_order_id", columnNames = "orderId")})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeductOrder extends AbstractEntity {

    private static final long serialVersionUID = -8248195399430097653L;
    /****
     * 扣款订单ID
     */
    @Column(length = 128, nullable = false, columnDefinition = "varchar(128) COMMENT '扣款订单ID'")
    private String orderId;

    /***
     * 业务订单号
     */
    @Column(length = 64, nullable = false, columnDefinition = "varchar(64) COMMENT '业务订单号'")
    private String outTradeNo;

    /***
     * 业务订单类型
     */
    @Column(length = 64, nullable = false, columnDefinition = "varchar(64) COMMENT '业务订单类型'")
    private String orderType;

    /***
     * 用户ID
     */
    @Column(length = 64, nullable = false, columnDefinition = "varchar(64) COMMENT '用户ID'")
    private String userId;

    /***
     * 扣款金额（单位：元，保留两位小数）
     */
    @Column(columnDefinition = "Decimal(19,2) default '0.00' COMMENT '扣款金额（单位：元，保留两位小数）'")
    private BigDecimal amount;

    /***
     * 扣款类型
     */
    @Column(length = 16, nullable = false, columnDefinition = "varchar(16) COMMENT '扣款类型' ")
    private String deductType;

    /**
     * 扣款状态码 0成功 1失败
     */
    @Column(length = 4, nullable = false, columnDefinition = "varchar(4) COMMENT '扣款状态码:0成功,1失败'")
    private String state;

    /**
     * 扣款货币类型 001 暴鸡币 002 暴击值
     */
    @Column(length = 16, nullable = false, columnDefinition = "varchar(16) COMMENT '扣款货币类型:001 暴鸡币,002 暴击值'")
    private String currencyType;

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
     * 描述：保存推送消息
     */
    @Column(columnDefinition = "longtext COMMENT '描述：保存推送消息'")
    private String description;

    /**
     * 账户余额(元）
     */
    @Transient
    private String balance;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getDeductType() {
        return deductType;
    }

    public void setDeductType(String deductType) {
        this.deductType = deductType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
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

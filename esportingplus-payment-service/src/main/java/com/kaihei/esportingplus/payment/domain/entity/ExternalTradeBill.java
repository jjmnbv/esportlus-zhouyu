package com.kaihei.esportingplus.payment.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 第三方支付流水表
 *
 * @author: tangtao
 **/
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "flow_no", columnNames = "flowNo")},
        indexes = {
                @Index(name = "idx_order_id", columnList = "orderId"),
                @Index(name = "idx_out_trade_no_and_order_type", columnList = "outTradeNo,orderType")
        })
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class ExternalTradeBill extends AbstractBillEntity {

    private static final long serialVersionUID = 4433125414716242885L;

    /***
     * 流水号
     */
    @Column(nullable = false, length = 128, columnDefinition = "varchar(128) COMMENT '流水号'")
    private String flowNo;

    /***
     * 支付订单id
     */
    @Column(nullable = false, columnDefinition = "varchar(128) COMMENT '支付订单id'")
    private String orderId;

    /***
     * 支付渠道
     */
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16) COMMENT '支付渠道'")
    private String channel;

    /***
     * 金额（单位：分）
     */
    @Column(nullable = false, columnDefinition = "int(10) COMMENT '金额(单位：分)'")
    private Integer totalFee;

    /***
     * 交易类型
     */
    @Column(nullable = false, length = 3, columnDefinition = "varchar(3) COMMENT '交易类型(T001 充值,T002 支付,T003 提现,T004 退款)'")
    private String tradeType;

    /***
     * 第三方支付流水号
     */
    @Column(nullable = false, length = 128, columnDefinition = "varchar(128) COMMENT '第三方支付流水号'")
    private String transactionId;

    /***
     * 业务单号
     */
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) COMMENT '业务单号'")
    private String outTradeNo;

    /***
     * 业务单据类型
     */
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16) COMMENT '业务单据类型'")
    private String orderType;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

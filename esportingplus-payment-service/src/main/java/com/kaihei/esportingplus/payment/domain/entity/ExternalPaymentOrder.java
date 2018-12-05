package com.kaihei.esportingplus.payment.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @program: esportingplus
 * @description: 支付订单表
 * @author: xusisi
 * @create: 2018-10-29 16:45
 **/
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "order_id", columnNames = "orderId")},

        indexes = {
                @Index(name = "idx_out_trade_no_and_order_type", columnList = "outTradeNo,orderType"),
                @Index(name = "idx_user_id_and_order_id", columnList = "userId,orderId")
        })
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class ExternalPaymentOrder extends AbstractEntity {

    /***
     * 附加数据
     */
    @Column(length = 512, columnDefinition = "varchar(512) default '' COMMENT '附加数据'")
    private String attach;

    /***
     * 内容
     */
    @Column(length = 1024, columnDefinition = "varchar(1024) default '' COMMENT '内容'")
    private String body;

    /***
     *支付渠道ID（来源表：pay_channel）
     */
    @Column(nullable = false, length = 20, columnDefinition = "bigint(20)  COMMENT '支付渠道ID'")
    private Long channelId;

    /***
     * 支付渠道名称
     * eg:alipay，wechat
     */
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16) COMMENT '支付渠道名称'")
    private String channelName;

    /***
     * 订单号
     */
    @Column(nullable = false, length = 128, columnDefinition = "varchar(128) COMMENT '订单号'")
    private String orderId;

    /***
     * 业务订单类型
     */
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16) COMMENT '用户ID'")
    private String orderType;

    /***
     * 业务订单号
     */
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) COMMENT '业务订单号'")
    private String outTradeNo;

    /***
     * 预支付订单号（微信使用的）
     */
    @Column(length = 64, columnDefinition = "varchar(64) COMMENT '预支付订单号（微信使用的）'")
    private String prePayId;

    /**
     * 支付完成时间
     */
    @Column(columnDefinition = "datetime COMMENT '支付完成时间,没有支付完成是1970-0-0 0:0:0'")
    private Date paiedTime;

    /***
     * 主题
     */
    @Column(length = 128, columnDefinition = "varchar(128) default '' COMMENT '主题'")
    private String subject;

    /***
     * 支付金额（单位：分）
     */
    @Column(nullable = false, length = 10, columnDefinition = "int(10) default 0 COMMENT '支付金额（单位：分）'")
    private Integer totalFee;

    /***
     * 交易状态
     * UNPAIED 订单待付款；
     * CLOSING 订单关闭中
     * CLOSED  订单已关闭（系统主动关闭、全额退款）；
     * SUCCESS 订单已付款；
     * CLOSED_NO_REFUND 交易结束，不可退款；
     * CANCELING 订单撤销中
     * CANCEL  订单已撤销（用户主动）。
     */
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '交易状态'")
    private String state;

    /***
     * 第三方返回的订单号
     */
    @Column(length = 128, columnDefinition = "varchar(128) default '' COMMENT '第三方返回的订单号'")
    private String transactionId;

    /***
     * 用户ID
     */
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '用户ID'")
    private String userId;

    /**
     * 来源应用ID（例如：baoji_ios、liudu_ios、android_hw、android_xx）
     */
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16) COMMENT ' 来源应用ID'")
    private String sourceAppId;

    /***
     * 货币类型：CN(人民币)
     */
    @Column(length = 16, columnDefinition = "varchar(16) COMMENT '货币类型'")
    private String currencyType;

    /***
     * 支付通知回调URL
     */
    @Column(nullable = false, length = 256, columnDefinition = "varchar(256) default '' COMMENT '支付通知回调URL'")
    private String notifyUrl;

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
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

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Date getPaiedTime() {
        return paiedTime;
    }

    public void setPaiedTime(Date paiedTime) {
        this.paiedTime = paiedTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSourceAppId() {
        return sourceAppId;
    }

    public void setSourceAppId(String sourceAppId) {
        this.sourceAppId = sourceAppId;
    }

    public String getPrePayId() {
        return prePayId;
    }

    public void setPrePayId(String prePayId) {
        this.prePayId = prePayId;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

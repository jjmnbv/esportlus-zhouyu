package com.kaihei.esportingplus.payment.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Date;

/**
 * 提现订单表
 * @author chenzhenjun
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(indexes = {@Index(name = "idx_out_trade_no", columnList = "outTradeNo")})
public class ExternalWithdrawOrder extends  AbstractEntity{

    private static final long serialVersionUID = 7086874584140076537L;

    /**
     * 业务订单号
     */
    @Column(nullable = false, length = 50, columnDefinition = "varchar(50) COMMENT '业务订单号'")
    private String outTradeNo;

    /**
     * 系统订单号
     */
    @Column(nullable = false, length = 50, columnDefinition = "varchar(50) COMMENT '订单号'")
    private String orderId;

    /**
     * 提现金额
     */
    @Column(nullable = false, length = 10, columnDefinition = "int(10) COMMENT '提现金额(单位:分)'")
    private Integer totalFee;

    /**
     * 付款时间
     */
    @Column(columnDefinition = "datetime DEFAULT NULL COMMENT '付款时间'")
    private Date paiedTime;

    /**
     * 业务订单号
     * PROCESSING 处理中
     * SUCCESS    已完成
     * FAILED     失败
     * CANCEL     已取消
     */
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16) COMMENT '状态(PROCESSING-处理中|SUCCESS-已完成|FAILED-失败|CANCEL-已取消)'")
    private String state;

    /**
     * 云经纪外部关联流水号
     */
    @Column(columnDefinition = "varchar(64) COMMENT '云经纪外部关联流水号'")
    private String refNo;

    /**
     * 云经纪外部关联流水号
     */
    @Column(columnDefinition = "varchar(64) COMMENT '系统打款商户订单号'")
    private String sysWalletRef;

    /**
     * 系统打款交易流水号
     */
    @Column(columnDefinition = "varchar(128) COMMENT '系统打款交易流水号'")
    private String sysBankBill;

    /**
     * 经纪公司打款商户订单号
     */
    @Column(columnDefinition = "varchar(64) COMMENT '经纪公司打款商户订单号'")
    private String brokerWalletRef;

    /**
     * 经纪公司打款交易流水号
     */
    @Column(columnDefinition = "varchar(64) COMMENT '经纪公司打款交易流水号'")
    private String brokerBankBill;

    /**
     * 用户提现银行卡号、支付宝账号、微信openid(三合一)
     */
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '用户提现银行卡号、支付宝账号、微信openid'")
    private String cardNo;

    /**
     * 用户外键id
     */
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '用户外键id'")
    private String userId;

    /**
     * 身份证号
     */
    @Column(nullable = false, length = 18, columnDefinition = "varchar(18) COMMENT '身份证号'")
    private String idcardNumber;

    /**
     * 真实姓名
     */
    @Column(columnDefinition = "varchar(32) COMMENT '真实姓名'")
    private String realName;

    /**
     * 支付渠道
     */
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16) COMMENT '支付渠道'")
    private String channel;

    /**
     * 失败状态码
     */
    @Column(length = 16, columnDefinition = "varchar(16) DEFAULT NULL COMMENT '失败状态码'")
    private String statusCode;

    /**
     * 状态码详细说明
     */
    @Column(length = 256, columnDefinition = "varchar(256) DEFAULT NULL COMMENT '状态码详细说明'")
    private String message;

    /**
     * 来源应用ID.e.g.baoji_ios、liudu_ios、android_hw、android_xx
     */
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16) COMMENT '来源应用ID'")
    private String sourceAppId;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public Date getPaiedTime() {
        return paiedTime;
    }

    public void setPaiedTime(Date paiedTime) {
        this.paiedTime = paiedTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getSysWalletRef() {
        return sysWalletRef;
    }

    public void setSysWalletRef(String sysWalletRef) {
        this.sysWalletRef = sysWalletRef;
    }

    public String getSysBankBill() {
        return sysBankBill;
    }

    public void setSysBankBill(String sysBankBill) {
        this.sysBankBill = sysBankBill;
    }

    public String getBrokerWalletRef() {
        return brokerWalletRef;
    }

    public void setBrokerWalletRef(String brokerWalletRef) {
        this.brokerWalletRef = brokerWalletRef;
    }

    public String getBrokerBankBill() {
        return brokerBankBill;
    }

    public void setBrokerBankBill(String brokerBankBill) {
        this.brokerBankBill = brokerBankBill;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIdcardNumber() {
        return idcardNumber;
    }

    public void setIdcardNumber(String idcardNumber) {
        this.idcardNumber = idcardNumber;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getSourceAppId() {
        return sourceAppId;
    }

    public void setSourceAppId(String sourceAppId) {
        this.sourceAppId = sourceAppId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

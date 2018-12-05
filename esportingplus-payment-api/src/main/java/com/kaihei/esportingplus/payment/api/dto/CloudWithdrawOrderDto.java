package com.kaihei.esportingplus.payment.api.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 查询提现订单——数据传输对象
 * @author chenzhenjun
 */
public class CloudWithdrawOrderDto implements Serializable {

    private static final long serialVersionUID = -6516474681501524941L;

    /**
     * 订单号
     */
    private String orderId;
    /**
     * 业务订单号
     */
    private String outTradeNo;

    /**
     * 提现金额
     */
    private Integer totalFee;

    /**
     * 付款时间
     */
    private Date paiedTime;

    /**
     * 业务订单号
     * PROCESSING 处理中
     * SUCCESS    已完成
     * FAILED     失败
     * CANCEL     已取消
     */
    private String state;

    /**
     * 云经纪外部关联流水号
     */
    private String refNo;

    /**
     * 云经纪外部关联流水号
     */
    private String sysWalletRef;

    /**
     * 系统打款交易流水号
     */
    private String sysBankBill;

    /**
     * 经纪公司打款商户订单号
     */
    private String brokerWalletRef;

    /**
     * 经纪公司打款交易流水号
     */
    private String brokerBankBill;

    /**
     * 用户提现银行卡号、支付宝账号、微信openid(三合一)
     */
    private String cardNo;

    /**
     * 用户外键id
     */
    private String userId;

    /**
     * 身份证号
     */
    private String idcardNumber;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 支付渠道
     */
    private String channel;

    /**
     * 失败状态码
     */
    private String statusCode;

    /**
     * 状态码详细说明
     */
    private String message;

    /**
     * 来源应用ID.e.g.baoji_ios、liudu_ios、android_hw、android_xx
     */
    private String sourceAppId;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSourceAppId() {
        return sourceAppId;
    }

    public void setSourceAppId(String sourceAppId) {
        this.sourceAppId = sourceAppId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

package com.kaihei.esportingplus.payment.api.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * @program: esportingplus
 * @description: 支付订单VO
 * @author: xusisi
 * @create: 2018-11-9
 **/

public class ExternalPaymentOrderVo implements Serializable {

    private static final long serialVersionUID = 2517879806312818661L;
    /***
     * 附加数据
     */
    private String attach;

    /***
     * 内容
     */
    private String body;

    /***
     *支付渠道ID（来源表：pay_channel）
     */
    private Long channelId;

    /***
     * 支付渠道名称
     * eg:alipay，wechat
     */
    private String channelName;

    /***
     * 订单号
     */
    private String orderId;

    /***
     * 业务订单类型
     */
    private String orderType;

    /***
     * 业务订单号
     */
    private String outTradeNo;

    /***
     * 预支付订单号（微信使用的）
     */
    private String prePayId;

    /**
     * 支付完成时间
     */
    private String paiedTime;

    /***
     * 主题
     */
    private String subject;

    /***
     * 支付金额（单位：分）
     */
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
    private String state;

    /***
     * 第三方返回的订单号
     */
    private String transactionId;

    /***
     * 用户ID
     */
    private String userId;

    /**
     * 来源应用ID（例如：baoji_ios、liudu_ios、android_hw、android_xx）
     */
    private String sourceAppId;

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

    public String getPaiedTime() {
        return paiedTime;
    }

    public void setPaiedTime(String paiedTime) {
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

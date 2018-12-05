package com.kaihei.esportingplus.payment.api.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * @Author: xusisi
 * @Date: 2018/11/9
 * @Description:退款订单VO
 */
public class ExternalRefundOrderVo implements Serializable {

    private static final long serialVersionUID = 8499765403623219212L;
    /***
     * 附加数据
     */
    private String attach;

    /***
     * 内容
     */
    private String body;

    /***
     * 商品类型
     */
    private String goodsType;

    /***
     * 退款订单号(由于订单生产方的订单会存在重复现象，给微信的outRefundNo实际上是我们本模块自己生成的，微信回调和请求退款的outRefundNo实际就是这个id，是比较坑，数据排查会绕很多弯路)
     */
    private String orderId;

    /***
     * 业务订单类型
     */
    private String orderType;

    /**
     * 原支付订单号
     */
    private String payOrderId;

    /***
     * 业务订单号
     */
    private String outTradeNo;

    /***
     * 业务退款订单号
     */
    private String outRefundNo;

    /***
     * 退款金额
     */
    private int totalFee;

    /**
     * 退款状态 REFUNDING 退款中； SUCCESS 退款成功； FAILED  退款失败。 CANCEL  订单已撤销（用户主动）
     */
    private String state;

    /**
     * 退款完成时间
     */
    private String refundTime;

    /***
     * 支付渠道ID（来源表：pay_channel）
     */
    private Long channelId;

    /***
     * 支付渠道名称
     */
    private String channelName;

    /***
     * 第三方返回的订单号
     */
    private String transactionId;

    /***
     * 来源应用ID
     */
    private String sourceAppId;

    /***
     * 用户ID
     */
    private String userId;

    /***
     *主题
     */
    private String subject;

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

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
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

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
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

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getSourceAppId() {
        return sourceAppId;
    }

    public void setSourceAppId(String sourceAppId) {
        this.sourceAppId = sourceAppId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

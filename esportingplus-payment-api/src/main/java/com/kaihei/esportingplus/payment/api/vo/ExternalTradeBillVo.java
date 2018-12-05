package com.kaihei.esportingplus.payment.api.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: tangtao
 **/
public class ExternalTradeBillVo implements Serializable {


    private static final long serialVersionUID = 3791531978035125303L;

    /***
     * 流水号
     */
    private String flowNo;

    /***
     * 支付订单id
     */
    private String orderId;

    /***
     * 支付渠道
     */
    private String channel;

    /***
     * 金额（单位：分）
     */
    private Integer totalFee;

    /***
     * 交易类型
     */
    private String tradeType;

    /***
     * 第三方支付流水号
     */
    private String transactionId;

    /***
     * 业务单号
     */
    private String outTradeNo;

    /***
     * 业务单据类型
     */
    private String orderType;

    /***
     * 创建时间
     */
    private Date createDate;

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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}

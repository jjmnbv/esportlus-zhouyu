package com.kaihei.esportingplus.payment.api.vo;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 暴鸡币支付订单支付信息
 *
 * @author: xusisi
 **/
public class GCoinPaymentVo {

    /***
     * 主题
     */
    private String subject = "";

    /***
     * 内容
     */
    private String body = "";

    /***
     * 描述
     */
    private String description = "";

    /***
     * 操作系统
     * H5表示h5、ANDROID表示android、IOS表示ios、MP表示小程序
     */
    private String sourceId;

    /***
     * 支付金额（单位：分）
     */
    private BigDecimal amount;

    /****
     * 业务订单号
     */
    private String outTradeNo;

    /***
     * 用户ID
     */
    private String userId;

    /****
     * 支付订单号
     */
    private String orderId;

    /****
     * 业务订单类型
     */
    private Integer orderType;

    /***
     * 附件数据（json字符串）
     */
    private String attach;

    /***
     * 支付订单状态码
     * 000支付失败
     * 001待支付
     * 002支付成功，业务订单待结算
     * 003支付成功，业务订单已完成
     * 004支付成功，退款中
     * 005已退款
     */
    private String state;

    /***
     * 失败状态码
     * 000(超时未支付)
     * 001（暴鸡币不足）
     * 002（账户被冻结）
     */
    private String errorCode;

    /***
     * 失败状态描述
     * 000超时未支付
     * 001暴鸡币不足
     * 002账户被冻结
     */
    private String errorMsg;

    /***
     * 支付完成时间
     */
    private String completedDate;

    /***
     * 已退金额（单位：元，保留两位小数）
     */
    private BigDecimal refundedAmount;

    /***
     * 退款中的金额（单位：元，保留两位小数）
     */
    private BigDecimal refundingAmount;

    /***
     * 回调通知URL
     */
    private String notifyUrl;

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

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public BigDecimal getRefundedAmount() {
        return refundedAmount;
    }

    public void setRefundedAmount(BigDecimal refundedAmount) {
        this.refundedAmount = refundedAmount;
    }

    public BigDecimal getRefundingAmount() {
        return refundingAmount;
    }

    public void setRefundingAmount(BigDecimal refundingAmount) {
        this.refundingAmount = refundingAmount;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

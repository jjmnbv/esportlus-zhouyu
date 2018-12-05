package com.kaihei.esportingplus.payment.api.vo;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 暴鸡币退款订单详情
 *
 * @author: xusisi
 **/
public class GCoinRefundVo {

    /***
     * 主题
     */
    private String subject;

    /***
     * 内容
     */
    private String body;

    /***
     * 描述
     */
    private String description;

    /***
     * 操作系统
     * H5表示h5、ANDROID表示android、IOS表示ios、MP表示小程序
     */
    private String sourceId;

    /***
     * 退款金额（单位：分）
     */
    private BigDecimal amount;

    /****
     * 业务订单号
     */
    private String outTradeNo;

    /**
     * 业务退款订单号
     */
    private String outRefundNo;

    /***
     * 用户ID
     */
    private String userId;

    /****
     * 退款订单号
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
     * 退款订单状态码
     * 000退款失败
     * 001退款中
     * 002已退款
     */
    private String state;

    /***
     * 退款完成时间
     */
    private String completedDate;

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

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
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

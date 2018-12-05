package com.kaihei.esportingplus.payment.api.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @program: esportingplus
 * @description: 退款操作返回
 * @author: xusisi
 * @create: 2018-10-09 12:20
 **/
public class DeductOrderVo {

    /***
     * 业务订单号
     */
    private String outTradeNo;

    /***
     * 业务订单类型
     */
    private String orderType;

    /***
     * 用户ID
     */
    private String userId;

    /***
     * 暴鸡币、暴击值扣款金额
     */
    private String amount;

    /***
     * 扣款类型
     */
    private String deductType;

    /***
     * 订单ID
     */
    private String orderId;

    /***
     * 状态 ：0扣款成功 ,1扣款失败
     */
    private String state;

    /***
     * 推送消息
     */
    private String message;

    /***
     * 货币类型：001暴鸡币，002暴击值
     */
    private String currencyType;


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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDeductType() {
        return deductType;
    }

    public void setDeductType(String deductType) {
        this.deductType = deductType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

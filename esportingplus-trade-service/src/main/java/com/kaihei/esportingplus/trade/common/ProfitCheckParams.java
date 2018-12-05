package com.kaihei.esportingplus.trade.common;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public final class ProfitCheckParams implements Serializable {

    //老板支付总金额
    private int paySum;
    //单个暴鸡收益
    private int profitAmout;
    private int userIdentity;
    private String uid;
    private long orderItermId;
    private String orderSequence;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getOrderItermId() {
        return orderItermId;
    }

    public void setOrderItermId(long orderItermId) {
        this.orderItermId = orderItermId;
    }

    public int getPaySum() {
        return paySum;
    }

    public void setPaySum(int paySum) {
        this.paySum = paySum;
    }

    public int getProfitAmout() {
        return profitAmout;
    }

    public int getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(int userIdentity) {
        this.userIdentity = userIdentity;
    }

    public void setProfitAmout(int profitAmout) {
        this.profitAmout = profitAmout;
    }

    public String getOrderSequence() {
        return orderSequence;
    }

    public void setOrderSequence(String orderSequence) {
        this.orderSequence = orderSequence;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

package com.kaihei.esportingplus.payment.api.vo;

import java.io.Serializable;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 返回给python分成计算
 *
 * @author: zhouyu
 */
public class InComeBenefitVo implements Serializable {

    private static final long serialVersionUID = -17903324859679188L;

    private String incomeOrderId;
    private int orderType;
    private int amount;
    private int state;
    private String incomeFinishDate;
    private String attach;

    public String getIncomeOrderId() {
        return incomeOrderId;
    }

    public void setIncomeOrderId(String incomeOrderId) {
        this.incomeOrderId = incomeOrderId;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getIncomeFinishDate() {
        return incomeFinishDate;
    }

    public void setIncomeFinishDate(String incomeFinishDate) {
        this.incomeFinishDate = incomeFinishDate;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

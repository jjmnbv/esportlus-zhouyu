package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;

public class StudioUserOrderStatisticVO implements Serializable {

    /**
     * 暴鸡Id
     */
    private String uid;
    /**
     * DNF订单总数(成功、失败)
     */
    private Long orderNumberAll;
    /**
     * DNF成功订单总数
     */
    private Long orderNumberSuccess;
    /**
     * DNF投诉订单总数
     */
    private Long orderNumberCompaint;
    /**
     * DNF涉及投诉订单金额
     */
    private Integer orderAmountCompaint;
    /**
     * 订单总收入
     */
    private Integer orderAmountIncome;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getOrderNumberAll() {
        return orderNumberAll;
    }

    public void setOrderNumberAll(Long orderNumberAll) {
        this.orderNumberAll = orderNumberAll;
    }

    public Long getOrderNumberSuccess() {
        return orderNumberSuccess;
    }

    public void setOrderNumberSuccess(Long orderNumberSuccess) {
        this.orderNumberSuccess = orderNumberSuccess;
    }

    public Long getOrderNumberCompaint() {
        return orderNumberCompaint;
    }

    public void setOrderNumberCompaint(Long orderNumberCompaint) {
        this.orderNumberCompaint = orderNumberCompaint;
    }

    public Integer getOrderAmountCompaint() {
        return orderAmountCompaint;
    }

    public void setOrderAmountCompaint(Integer orderAmountCompaint) {
        this.orderAmountCompaint = orderAmountCompaint;
    }

    public Integer getOrderAmountIncome() {
        return orderAmountIncome;
    }

    public void setOrderAmountIncome(Integer orderAmountIncome) {
        this.orderAmountIncome = orderAmountIncome;
    }
}

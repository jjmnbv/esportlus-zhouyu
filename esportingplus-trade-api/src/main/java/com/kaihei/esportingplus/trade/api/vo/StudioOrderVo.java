package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;

public class StudioOrderVo implements Serializable {

    /**
     * 用户uid
     */
    private String uid;
    /**
     * 订单sequeue
     */
    private String sequeue;
    /**
     * 订单状态
     */
    private Byte status;
    /**
     * 订单价格
     */
    private Integer price;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSequeue() {
        return sequeue;
    }

    public void setSequeue(String sequeue) {
        this.sequeue = sequeue;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}

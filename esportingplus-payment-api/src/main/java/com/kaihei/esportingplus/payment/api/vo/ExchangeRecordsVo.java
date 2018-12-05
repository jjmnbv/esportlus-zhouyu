package com.kaihei.esportingplus.payment.api.vo;

import java.math.BigDecimal;

/**
 * 暴击值兑换记录-响应Vo
 * @author chenzhenjun
 */
public class ExchangeRecordsVo {

    private long id;

    private String uid;

    private BigDecimal starlight;

    private BigDecimal gcoin;

    private String exchangeDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public BigDecimal getStarlight() {
        return starlight;
    }

    public void setStarlight(BigDecimal starlight) {
        this.starlight = starlight;
    }

    public BigDecimal getGcoin() {
        return gcoin;
    }

    public void setGcoin(BigDecimal gcoin) {
        this.gcoin = gcoin;
    }

    public String getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(String exchangeDate) {
        this.exchangeDate = exchangeDate;
    }
}

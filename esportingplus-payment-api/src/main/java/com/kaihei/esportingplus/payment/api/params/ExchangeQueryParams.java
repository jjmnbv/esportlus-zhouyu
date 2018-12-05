package com.kaihei.esportingplus.payment.api.params;

import java.io.Serializable;

/**
 * 后台查询暴击值兑换参数
 */
public class ExchangeQueryParams implements Serializable {

    private String uid;

    private String beginDate;

    private String endDate;

    private String page;

    private String size;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}

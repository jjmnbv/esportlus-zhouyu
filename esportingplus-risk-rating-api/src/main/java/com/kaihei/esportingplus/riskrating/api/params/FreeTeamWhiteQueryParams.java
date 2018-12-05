package com.kaihei.esportingplus.riskrating.api.params;

import java.io.Serializable;

/**
 * 白名单列表查询参数
 * @author chenzhenjun
 */
public class FreeTeamWhiteQueryParams implements Serializable {

    private static final long serialVersionUID = -1936032021630514910L;

    private String deviceId;

    private String beginDate;

    private String endDate;

    private String page;

    private String size;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

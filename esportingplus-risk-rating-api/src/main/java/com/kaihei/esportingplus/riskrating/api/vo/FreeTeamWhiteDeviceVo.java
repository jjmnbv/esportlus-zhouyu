package com.kaihei.esportingplus.riskrating.api.vo;

import java.io.Serializable;

/**
 * 白名单列表返回
 * @author chenzhenjun
 */
public class FreeTeamWhiteDeviceVo implements Serializable {

    private long id;

    private String deviceId;

    private String createDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}

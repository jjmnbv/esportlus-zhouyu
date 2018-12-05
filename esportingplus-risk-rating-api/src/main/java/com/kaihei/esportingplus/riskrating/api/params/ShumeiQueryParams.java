package com.kaihei.esportingplus.riskrating.api.params;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 拼凑查询数美接口参数
 * @author chenzhenjun
 */
public class ShumeiQueryParams implements Serializable {

    private static final long serialVersionUID = 5662553128023099487L;
    private String accessKey;

    private String shumeiBaseUrl;

    private String phone;

    private String deviceId;

    private String eventType;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getShumeiBaseUrl() {
        return shumeiBaseUrl;
    }

    public void setShumeiBaseUrl(String shumeiBaseUrl) {
        this.shumeiBaseUrl = shumeiBaseUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

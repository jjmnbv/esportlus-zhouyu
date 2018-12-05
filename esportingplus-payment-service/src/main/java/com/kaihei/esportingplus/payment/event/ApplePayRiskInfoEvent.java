package com.kaihei.esportingplus.payment.event;

import com.kaihei.esportingplus.common.event.Event;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 苹果支付风控信息事件
 *
 * @author xusisi
 */
public class ApplePayRiskInfoEvent implements Event {

    private String sourceId;
    private String currencyType;
    private String uid;
    private Integer amount;
    private String deviceNo;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

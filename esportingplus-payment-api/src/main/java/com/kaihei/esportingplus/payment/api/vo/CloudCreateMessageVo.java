package com.kaihei.esportingplus.payment.api.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 消费者调用云账户提现Api消息Vo
 * @author chenzhenjun
 */
public class CloudCreateMessageVo implements Serializable {

    private static final long serialVersionUID = 7346850628052156103L;

    private String outTradeNo;

    private String cloudUrl;

    private String dealerId;

    private String brokerId;

    private String notifyUrl;

    /**
     * 渠道value
     */
    private String channel;

    private String descKey;

    private String appKey;

    private String appId;

    private String ip;

    public CloudCreateMessageVo() {
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getCloudUrl() {
        return cloudUrl;
    }

    public void setCloudUrl(String cloudUrl) {
        this.cloudUrl = cloudUrl;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(String brokerId) {
        this.brokerId = brokerId;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDescKey() {
        return descKey;
    }

    public void setDescKey(String descKey) {
        this.descKey = descKey;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

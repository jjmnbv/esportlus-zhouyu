package com.kaihei.esportingplus.payment.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 云账户配置表
 * @author chenzhenjun
 */
@Entity
public class CapaySetting extends  AbstractEntity{

    private static final long serialVersionUID = -4704765786044568148L;

    /**
     * 商户代码
     */
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) COMMENT '商户代码'")
    private String dealerId;

    /**
     * 代征主体
     */
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) COMMENT '代征主体'")
    private String brokerId;

    /**
     * 提现到银行卡时的URL
     */
    @Column(nullable = false, length = 128, columnDefinition = "varchar(128) COMMENT '提现到银行卡时的URL'")
    private String bankCardUrl;

    /**
     * 提现到支付宝时的URL
     */
    @Column(nullable = false, length = 128, columnDefinition = "varchar(128) COMMENT '提现到支付宝时的URL'")
    private String alipayUrl;

    /**
     * 提现到微信时的URL
     */
    @Column(nullable = false, length = 128, columnDefinition = "varchar(128) COMMENT '提现到微信时的URL'")
    private String wechatPayUrl;

    /**
     * 加密密钥
     */
    @Column(nullable = false, length = 256, columnDefinition = "varchar(256) COMMENT '加密密钥'")
    private String descKey;

    /**
     * 签名
     */
    @Column(nullable = false, length = 256, columnDefinition = "varchar(256) COMMENT '签名'")
    private String appKey;

    /**
     * 云账户提现支持渠道-目前支持微信及支付宝
     */
    @Column(length = 64, columnDefinition = "varchar(64) COMMENT '云账户提现支持渠道(wx,zfb)'")
    private String supportChannel;

    /**
     * 回调URL
     */
    @Column(length = 256, columnDefinition = "varchar(256) COMMENT '云账户回调URL'")
    private String notifyUrl;

    /**
     * 支付渠道ID（pay_channel主键–可供接入支付渠道表）
     */
    @Column(nullable = false, length = 20, columnDefinition = "bigint(20)  COMMENT '支付渠道ID'")
    private Long channelId;

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

    public String getBankCardUrl() {
        return bankCardUrl;
    }

    public void setBankCardUrl(String bankCardUrl) {
        this.bankCardUrl = bankCardUrl;
    }

    public String getAlipayUrl() {
        return alipayUrl;
    }

    public void setAlipayUrl(String alipayUrl) {
        this.alipayUrl = alipayUrl;
    }

    public String getWechatPayUrl() {
        return wechatPayUrl;
    }

    public void setWechatPayUrl(String wechatPayUrl) {
        this.wechatPayUrl = wechatPayUrl;
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

    public String getSupportChannel() {
        return supportChannel;
    }

    public void setSupportChannel(String supportChannel) {
        this.supportChannel = supportChannel;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

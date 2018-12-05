package com.kaihei.esportingplus.payment.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @program: esportingplus
 * @description: 支付宝配置表
 * @author: xusisi
 * @create: 2018-10-29 17:22
 **/
@Entity
@Table
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlipaySetting extends AbstractEntity {

    /***
     * 商户app_id
     */
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) COMMENT '商户app_id'")
    private String appId;

    /***
     * 商户私钥
     */
    @Column(nullable = false, length = 2048, columnDefinition = "varchar(2048)  COMMENT '商户私钥'")
    private String rsaPrivateKey;

    /***
     * Alipay公钥
     */
    @Column(nullable = false, length = 2048, columnDefinition = "varchar(2048) COMMENT 'Alipay公钥'")
    private String alipayPublicKey;

    /***
     * 回调URL
     */
    @Column(nullable = false, length = 256, columnDefinition = "varchar(256)  COMMENT '回调URL'")
    private String notifyUrl;

    /**
     * 请求URL
     */
    @Column(nullable = false, length = 256, columnDefinition = "varchar(256)  COMMENT '请求URL'")
    private String requestUrl;

    /***
     * 编码方式
     */
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16)  COMMENT '编码方式'")
    private String charset;

    /***
     * 返回格式
     */
    @Column(nullable = false, length = 40, columnDefinition = "varchar(40)   COMMENT '返回格式'")
    private String format;

    /***
     * 加密方式
     */
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16)  COMMENT '加密方式'")
    private String signType;

    /***
     * 该笔订单允许的最晚付款时间，逾期将关闭交易。
     * 1m～15d
     */
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16) default '15d' COMMENT '该笔订单允许的最晚付款时间，逾期将关闭交易。默认是15天，交易就会关闭'")
    private String timeoutExpress;

    /***
     * 销售产品码
     */
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64)  COMMENT '销售产品码'")
    private String productCode;

    /***
     * 支付渠道ID
     */
    @Column(nullable = false, length = 20, columnDefinition = "bigint(20)  COMMENT '支付渠道ID'")
    private Long channelId;

    /***
     * 正式环境URL
     */
    @Column(length = 256, columnDefinition = "varchar(256) default '' COMMENT '正式环境URL'")
    private String formalUrl;

    /***
     * 沙盒测试环境地址
     */
    @Column(nullable = false, length = 256, columnDefinition = "varchar(256) COMMENT '沙盒测试环境地址'")
    private String sandboxUrl;

    /***
     * 是否开启沙盒环境
     * true 开启
     * false 禁用
     */
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16) default 'true' COMMENT '是否开启沙盒环境(true 开启,false 禁用)'")
    private String sandboxEnable;

    /***
     * 可用渠道说明
     * 用户只能在指定渠道范围内支付
     * 当有多个渠道时用“,”分隔
     * 注，与disablePayChannels互斥
     */
    @Column(length = 256, columnDefinition = "varchar(256) COMMENT '可用渠道说明'")
    private String enablePayChannels;

    /***
     * 禁用渠道
     * 用户不可用指定渠道支付
     * 当有多个渠道时用“,”分隔
     * 注，与enablePayChannels互斥
     */
    @Column(length = 256, columnDefinition = "varchar(256) COMMENT '禁用渠道'")
    private String disablePayChannels;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getRsaPrivateKey() {
        return rsaPrivateKey;
    }

    public void setRsaPrivateKey(String rsaPrivateKey) {
        this.rsaPrivateKey = rsaPrivateKey;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public void setAlipayPublicKey(String alipayPublicKey) {
        this.alipayPublicKey = alipayPublicKey;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getTimeoutExpress() {
        return timeoutExpress;
    }

    public void setTimeoutExpress(String timeoutExpress) {
        this.timeoutExpress = timeoutExpress;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getFormalUrl() {
        return formalUrl;
    }

    public void setFormalUrl(String formalUrl) {
        this.formalUrl = formalUrl;
    }

    public String getSandboxUrl() {
        return sandboxUrl;
    }

    public void setSandboxUrl(String sandboxUrl) {
        this.sandboxUrl = sandboxUrl;
    }

    public String getSandboxEnable() {
        return sandboxEnable;
    }

    public void setSandboxEnable(String sandboxEnable) {
        this.sandboxEnable = sandboxEnable;
    }

    public String getEnablePayChannels() {
        return enablePayChannels;
    }

    public void setEnablePayChannels(String enablePayChannels) {
        this.enablePayChannels = enablePayChannels;
    }

    public String getDisablePayChannels() {
        return disablePayChannels;
    }

    public void setDisablePayChannels(String disablePayChannels) {
        this.disablePayChannels = disablePayChannels;
    }
}

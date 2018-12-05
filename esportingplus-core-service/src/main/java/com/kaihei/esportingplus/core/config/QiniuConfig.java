package com.kaihei.esportingplus.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author zl.zhao
 * @description:
 * @date: 2018/10/30 17:45
 */
@Configuration
public class QiniuConfig {
    @Value("${qiniu.domain.baojiPubHttps}")
    private String baojiPubHttps;

    @Value("${qiniu.domain.baoji}")
    private String baojiDomain;

    @Value("${qiniu.domain.baojiPub}")
    private String baojiPub;

    @Value("${qiniu.domain.bnPubBucket}")
    private String bnPubBucket;

    @Value("${qiniu.domain.discoveryBucket}")
    private String discoveryBucket;

    @Value("${qiniu.domain.discoveryVideoBucket}")
    private String discoveryVideoBucket;

    public String getBaojiPubHttps() {
        return baojiPubHttps;
    }

    public void setBaojiPubHttps(String baojiPubHttps) {
        this.baojiPubHttps = baojiPubHttps;
    }

    public String getBaojiDomain() {
        return baojiDomain;
    }

    public void setBaojiDomain(String baojiDomain) {
        this.baojiDomain = baojiDomain;
    }

    public String getBaojiPub() {
        return baojiPub;
    }

    public void setBaojiPub(String baojiPub) {
        this.baojiPub = baojiPub;
    }

    public String getBnPubBucket() {
        return bnPubBucket;
    }

    public void setBnPubBucket(String bnPubBucket) {
        this.bnPubBucket = bnPubBucket;
    }

    public String getDiscoveryBucket() {
        return discoveryBucket;
    }

    public void setDiscoveryBucket(String discoveryBucket) {
        this.discoveryBucket = discoveryBucket;
    }

    public String getDiscoveryVideoBucket() {
        return discoveryVideoBucket;
    }

    public void setDiscoveryVideoBucket(String discoveryVideoBucket) {
        this.discoveryVideoBucket = discoveryVideoBucket;
    }
}

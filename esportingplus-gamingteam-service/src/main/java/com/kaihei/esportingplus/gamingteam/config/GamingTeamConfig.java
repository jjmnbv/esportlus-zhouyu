package com.kaihei.esportingplus.gamingteam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 车队配置
 * @author java developer
 */
@Configuration
public class GamingTeamConfig {

    /** 二维码邀请链接 */
    @Value("${gamingteam.small.program.invitation.url}")
    private String smallProgramInvitationUrl;

    /** 老板支付超时时间 */
    @Value("${gamingteam.payment.timeout}")
    private Integer paymentTimeout;

    /** 车队数据缓存过期时间 */
    @Value("${gamingteam.expire.time}")
    private Integer expireTime;

    /** 批量查询用户服务的 url */
    @Value("${gamingteam.user.detail.batch.query.url}")
    private String userDetailBatchQueryUrl;

    public String getSmallProgramInvitationUrl() {
        return smallProgramInvitationUrl;
    }

    public void setSmallProgramInvitationUrl(String smallProgramInvitationUrl) {
        this.smallProgramInvitationUrl = smallProgramInvitationUrl;
    }

    public Integer getPaymentTimeout() {
        return paymentTimeout;
    }

    public void setPaymentTimeout(Integer paymentTimeout) {
        this.paymentTimeout = paymentTimeout;
    }

    public Integer getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Integer expireTime) {
        this.expireTime = expireTime;
    }

    public String getUserDetailBatchQueryUrl() {
        return userDetailBatchQueryUrl;
    }

    public void setUserDetailBatchQueryUrl(String userDetailBatchQueryUrl) {
        this.userDetailBatchQueryUrl = userDetailBatchQueryUrl;
    }
}

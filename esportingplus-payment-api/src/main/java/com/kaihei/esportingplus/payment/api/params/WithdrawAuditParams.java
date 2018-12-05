package com.kaihei.esportingplus.payment.api.params;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 创建提现审核订单参数
 *
 * @author chenzhenjun
 */
public class WithdrawAuditParams implements Serializable {

    private static final long serialVersionUID = -1736330100152672921L;

    /**
     * 用户id(8位编码)
     */
    private String uid;

    /**
     * 用户id(主键)
     */
    private Integer userId;

    /**
     * 提现金额(单位：分)
     */
    private Integer amount;

    /**
     * 提现类型
     */
    private Integer transferType;

    /**
     * 主题
     */
    private String subject;

    /**
     * 包名appid
     */
    private String appId;

    /**
     * 客户端ip
     */
    private String clientIp;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getTransferType() {
        return transferType;
    }

    public void setTransferType(Integer transferType) {
        this.transferType = transferType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}

package com.kaihei.esportingplus.payment.api.params;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

/**
 * 云账户提现-请求参数
 * @author chenzhenjun
 */
@Validated
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CloudAccountOrderParams implements Serializable {

    private static final long serialVersionUID = -7382532740635333646L;

    private String userId;

    /**
     * bank-银行卡 wx-微信 zfb-支付宝
     */
    private String channel;

    private String outTradeNo;
    /**
     * 提现总金额(单位:分)
     */
    private String totalFee;

    private String realName;

    /**
     * 对应渠道
     * bank-银行卡号 wx-微信id zfb-支付宝账号
     */
    private String cardNo;

    private String idCard;

    public CloudAccountOrderParams(){}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

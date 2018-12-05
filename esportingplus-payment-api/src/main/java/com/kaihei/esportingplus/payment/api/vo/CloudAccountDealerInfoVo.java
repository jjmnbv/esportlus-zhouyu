package com.kaihei.esportingplus.payment.api.vo;

import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * @program: esportingplus
 * @description: 商户余额信息
 * @author: chenzhenjun
 * @create: 2018-11-05 18:11
 **/
public class CloudAccountDealerInfoVo implements Serializable {

    private static final long serialVersionUID = -1849846937412270452L;
    /**
     * 代征主体ID
     */
    private String brokerId;

    /**
     * 银行卡余额
     */
    private String bankCardBalance;

    /**
     * 是否开通银⾏卡通道
     */
    @JSONField(name="isBankCard")
    private Boolean isBankCard;

    /**
     * 支付宝余额
     */
    private String alipayBalance;

    /**
     * 是否开通⽀付宝通道
     */
    @JSONField(name="isAlipay")
    private Boolean isAlipay;

    /**
     * 微信余额
     */
    private String wxpayBalance;

    /**
     * 是否开通微信通道
     */
    @JSONField(name="isWxpay")
    private Boolean isWxpay;

    /**
     * 服务费返点余额
     */
    private String rebateFeeBalance;

    public String getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(String brokerId) {
        this.brokerId = brokerId;
    }

    public String getBankCardBalance() {
        return bankCardBalance;
    }

    public void setBankCardBalance(String bankCardBalance) {
        this.bankCardBalance = bankCardBalance;
    }

    public boolean getIsBankCard() {
        return isBankCard;
    }

    public void setIsBankCard(boolean bankCard) {
        isBankCard = bankCard;
    }

    public String getAlipayBalance() {
        return alipayBalance;
    }

    public void setAlipayBalance(String alipayBalance) {
        this.alipayBalance = alipayBalance;
    }

    public boolean getIsAlipay() {
        return isAlipay;
    }

    public void setIsAlipay(boolean alipay) {
        isAlipay = alipay;
    }

    public String getWxpayBalance() {
        return wxpayBalance;
    }

    public void setWxpayBalance(String wxpayBalance) {
        this.wxpayBalance = wxpayBalance;
    }

    public boolean getIsWxpay() {
        return isWxpay;
    }

    public void setIsWxpay(boolean wxpay) {
        isWxpay = wxpay;
    }

    public String getRebateFeeBalance() {
        return rebateFeeBalance;
    }

    public void setRebateFeeBalance(String rebateFeeBalance) {
        this.rebateFeeBalance = rebateFeeBalance;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

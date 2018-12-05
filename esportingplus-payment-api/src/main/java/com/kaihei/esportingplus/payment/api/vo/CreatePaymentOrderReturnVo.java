package com.kaihei.esportingplus.payment.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zhouyu
 * @Date: 2018/10/30 14:22
 * @Description:创建订单返回参数
 */
@Data
public class CreatePaymentOrderReturnVo implements Serializable{


    /***
     * 支付渠道Tag
     */
    private String channelTag;

    /**
     * 签名数据
     */
    private String sign;

    /***
     * 预支付订单，支付宝支付的可以为空
     */
    private String prepayId;

    public CreatePaymentOrderReturnVo(String channelTag, String sign) {
        this.channelTag = channelTag;
        this.sign = sign;
    }

    public CreatePaymentOrderReturnVo(String channelTag, String sign, String prepayId) {
        this.channelTag = channelTag;
        this.sign = sign;
        this.prepayId = prepayId;
    }

    public CreatePaymentOrderReturnVo() {
    }
}

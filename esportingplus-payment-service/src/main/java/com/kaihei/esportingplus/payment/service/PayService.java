package com.kaihei.esportingplus.payment.service;

import com.alipay.api.AlipayApiException;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.payment.api.params.*;
import com.kaihei.esportingplus.payment.api.vo.CloudAccountRespVo;
import com.kaihei.esportingplus.payment.api.vo.CreateRefundOrderReturnVo;
import com.kaihei.esportingplus.payment.domain.entity.AbstractEntity;
import com.kaihei.esportingplus.payment.domain.entity.ExternalPaymentOrder;
import com.kaihei.esportingplus.payment.domain.entity.ExternalRefundOrder;

import java.util.Map;

/**
 * @program: esportingplus
 * @description: 支付service
 * @author: xusisi
 * @create: 2018-10-15 21:05
 **/
public interface PayService {

    /**
     * 创建支付订单信息
     *
     * @param : [payOrderParams, appId, channelTag, paySetting, ip, area]
     * @Author : xusisi
     **/
    Map<String, String> createPaymentOrder(PayOrderParams payOrderParams, String appId, String channelTag, AbstractEntity paySetting, String ip,
                                           String area) throws BusinessException;

    /**
     * 查询支付结果
     *
     * @param : [outTradeNo, appId, channelTag, paySetting, orderType]
     * @Author : xusisi
     **/
    ExternalPaymentOrder searchPaymentOrderInfo(String outTradeNo, String appId, String channelTag, AbstractEntity paySetting, String orderType)
            throws BusinessException;

    /**
     * 创建退款订单信息
     *
     * @param : [refundOrderParams, appId, channelTag, paySetting]
     * @Author : xusisi
     **/
    CreateRefundOrderReturnVo createRefundOrder(RefundOrderParams refundOrderParams, String appId, String channelTag, AbstractEntity paySetting)
            throws BusinessException;

    /**
     * 查询退款结果
     *
     * @param : [outRefundNo, appId, channelTag, paySetting]
     * @Author : xusisi
     **/
    ExternalRefundOrder searchRefundOrderInfo(String outRefundNo, String appId, String channelTag, AbstractEntity paySetting)
            throws BusinessException;

    /**
     * 关闭支付订单
     *
     * @param : [payOrderParams, appId, channelTag, paySetting]
     * @Author : xusisi
     **/
    ExternalPaymentOrder closePaymentOrder(CloseOrCancelPayOrderParams closeOrCancelPayOrderParams, String appId, String channelTag,
                                           AbstractEntity paySetting)
            throws BusinessException;

    /**
     * 撤销支付订单
     *
     * @param : [payOrderParams, appId, channelTag, paySetting]
     * @Author : xusisi
     **/
    ExternalPaymentOrder cancelPaymentOrder(CloseOrCancelPayOrderParams closeOrCancelPayOrderParams, String appId, String channelTag,
                                            AbstractEntity paySetting)
            throws BusinessException;

    /**
     * 支付宝回调通知支付情况
     *
     * @param : [params, appId, channelTag, paySetting]
     * @Author : xusisi
     **/
    Boolean handleNotify(Map<String, String> params, String appId, String channelTag, AbstractEntity paySetting, String type) throws BusinessException,
            AlipayApiException;

    /**
     * 创建云账户提现订单
     *
     * @param : [orderParams, appId, channelTag, paySetting, ip]
     * @Author : xusisi
     **/
    public CloudAccountRespVo createWithdrawOrder(CloudAccountOrderParams orderParams, String appId, String channelTag,
                                                  AbstractEntity paySetting, String ip) throws BusinessException;

    /**
     * 内部支付-暴鸡币支付回调方法
     *
     * @param : [params]
     * @Author : xusisi
     **/
    public void walletNotify(Map<String, String> params);

}

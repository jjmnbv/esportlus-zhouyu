package com.kaihei.esportingplus.payment.service;

import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.payment.api.params.GCoinPaymentCreateParams;
import com.kaihei.esportingplus.payment.api.params.GCoinPaymentUpdateParams;
import com.kaihei.esportingplus.payment.api.vo.GCoinPaymentPreVo;
import com.kaihei.esportingplus.payment.api.vo.GCoinPaymentVo;
import com.kaihei.esportingplus.payment.domain.entity.GCoinBalance;
import com.kaihei.esportingplus.payment.domain.entity.GCoinPaymentOrder;
import com.kaihei.esportingplus.payment.mq.message.MQSimpleOrder;

/***
 * 暴鸡币支付相关服务类
 */
public interface GCoinPaymentService {

    public GCoinPaymentPreVo preCreatePayment(GCoinPaymentCreateParams gcoinPaymentCreateParams) throws BusinessException;

    public GCoinPaymentVo updateOrderInfo(GCoinPaymentUpdateParams gcoinPaymentUpdateParams) throws BusinessException;

    public GCoinPaymentOrder getPaymentInfo(String orderId, String userId);

    public GCoinPaymentOrder getPaymentInfoFromRedis(String orderId, String userId);

    public void refreshPaymentInfo(String orderId, String userId);

    public GCoinPaymentVo getGcoinPaymentVo(String orderId, String userId);

    public GCoinPaymentOrder checkOrderState(String orderId, String userId) throws BusinessException;

    public GCoinPaymentOrder checkOrderState(String orderId) throws BusinessException;

    public GCoinBalance checkGcoinAccountState(String userId)throws BusinessException;

    public void updatePaymentInfo(String orderId);

    public GCoinPaymentVo setErrorInfo(GCoinPaymentVo vo, BusinessException e);

    public GCoinPaymentVo getVo(String orderId, String orderType, String outTradeNo)throws BusinessException;

    public Boolean consumePaymentMQ(MQSimpleOrder mqSimpleOrder);
}

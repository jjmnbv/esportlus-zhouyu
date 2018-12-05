package com.kaihei.esportingplus.payment.service;

import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.payment.api.params.GCoinRefundParams;
import com.kaihei.esportingplus.payment.api.vo.GCoinRefundPreVo;
import com.kaihei.esportingplus.payment.api.vo.GCoinRefundVo;
import com.kaihei.esportingplus.payment.domain.entity.GCoinPaymentOrder;
import com.kaihei.esportingplus.payment.domain.entity.GCoinRefundOrder;
import com.kaihei.esportingplus.payment.mq.message.MQSimpleOrder;

public interface GCoinRefundService {

    public GCoinRefundPreVo createRefund(GCoinRefundParams gcoinRefundParams) throws BusinessException;

    public GCoinRefundVo getRefundVo(String orderId, String orderType, String outRefundNo);

    public GCoinRefundVo getRefundVo(String orderId);

    public void refreshRefundOrder(String orderId, String userId);

    public GCoinPaymentOrder checkGcoinPaymentState(String orderType, String outTradeNo);

    public GCoinRefundOrder checkGcoinRefundState(String orderId)throws Exception;

    public void updateRefundInfo(String orderId)throws BusinessException;

    public Boolean consumeRefundMQ(MQSimpleOrder mqSimpleOrder);

}

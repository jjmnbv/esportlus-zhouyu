package com.kaihei.esportingplus.payment.service;

import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.payment.api.params.GCoinRechargeCreateParams;
import com.kaihei.esportingplus.payment.api.params.GCoinRechargeUpdateParams;
import com.kaihei.esportingplus.payment.api.vo.GCoinRechargePreVo;
import com.kaihei.esportingplus.payment.api.vo.GCoinRechargeVo;
import com.kaihei.esportingplus.payment.domain.entity.GCoinRechargeOrder;
import com.kaihei.esportingplus.payment.mq.message.MQSimpleOrder;

import java.util.Map;

public interface GCoinRechargeService {

    public GCoinRechargePreVo preCreatePayment(GCoinRechargeCreateParams rechargeCreateParams) throws BusinessException;

    public GCoinRechargeVo updateOrderInfo(String orderId, GCoinRechargeUpdateParams rechargeUpdateParams) throws BusinessException;

    public GCoinRechargeVo getRechargeVo(String orderId, String userId);

    public GCoinRechargeOrder getOrderInfoFromRedis(String orderId, String userId);

    public void refreshOrderInfo(String orderId, String userId);

    public GCoinRechargeOrder getRechargeOrder(String orderId, String userId);

    public void updateRechargeInfo(String orderId) throws BusinessException, Exception;

    public GCoinRechargeOrder checkOrderState(String orderId) throws BusinessException;

    public GCoinRechargeOrder checkOrderState(String orderId, String userId) throws BusinessException;

    public GCoinRechargeVo entityTransformVo(GCoinRechargeOrder recharge);

    public Map<String, Object> findAllSuccessByCondition(String userId, String channel, String sourceId, String beginDate, String endDate,
                                                         String rechargeType, int page, int size);

    public Map<String, Integer> findAllSuccessSumInfoByCondition(String userId, String channel, String sourceId, String beginDate, String endDate,
                                                                 String rechargeType);

    public Boolean consumeRechargeMQ(MQSimpleOrder mqSimpleOrder);
}

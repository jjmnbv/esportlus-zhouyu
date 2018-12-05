package com.kaihei.esportingplus.payment.service;

import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.payment.api.params.DeductParams;
import com.kaihei.esportingplus.payment.api.params.ExchangeQueryParams;
import com.kaihei.esportingplus.payment.api.params.GCoinBackStageRechargeParam;
import com.kaihei.esportingplus.payment.api.vo.DeductOrderVo;
import com.kaihei.esportingplus.payment.api.vo.FrontGCcoinRechargeVo;
import com.kaihei.esportingplus.payment.api.vo.GCoinRechargeVo;

import java.util.Map;

public interface BackStagePaymentService {

    public GCoinRechargeVo createGcoinRecharge(GCoinBackStageRechargeParam gCoinBackStageRechargeParam) throws BusinessException;

    public DeductOrderVo createDeductOrder(DeductParams deductParams) throws BusinessException;

    public FrontGCcoinRechargeVo getGCoinRechargeList(String userId, String channel, String sourceId,
                                                      String beginDate, String endDate, String page, String size) throws BusinessException;

    public Map<String, Object> getStarlightExchangeList(ExchangeQueryParams queryParams) throws BusinessException;;


}

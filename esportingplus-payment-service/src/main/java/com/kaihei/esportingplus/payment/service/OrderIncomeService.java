package com.kaihei.esportingplus.payment.service;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.payment.api.params.OrderIncomeParams;
import com.kaihei.esportingplus.payment.api.params.OrderQueryParam;
import com.kaihei.esportingplus.payment.api.params.WorkRoomQueryParams;
import com.kaihei.esportingplus.payment.api.vo.InComeBenefitVo;
import java.util.List;
import java.util.Map;

/**
 * 订单结算服务接口
 *
 * @author zhouyu, haycco
 */
public interface OrderIncomeService {

    ResponsePacket<List<InComeBenefitVo>> insertBatchRateOrderIncome(List<OrderIncomeParams> incomeParamsList, int type);

    ResponsePacket<Map<String, Object>> getOrderIncomDetail(List<OrderQueryParam> queryParamList);

    ResponsePacket<Map<String, Object>> getUserAmountInLimitTime(String userId, Long timeStramp, Integer timelimit);

    ResponsePacket<Map<String, Object>> getWorkRoomIncome(List<WorkRoomQueryParams> workRoomQueryParams);

}

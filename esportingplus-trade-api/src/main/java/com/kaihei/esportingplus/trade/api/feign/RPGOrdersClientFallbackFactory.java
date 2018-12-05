package com.kaihei.esportingplus.trade.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.trade.api.params.CheckTeamMemberPayedParams;
import com.kaihei.esportingplus.trade.api.params.OrderParams;
import com.kaihei.esportingplus.trade.api.params.OrderQueryParams;
import com.kaihei.esportingplus.trade.api.params.RPGInComeParams;
import com.kaihei.esportingplus.trade.api.params.StudioOrderQueryParams;
import com.kaihei.esportingplus.trade.api.params.StudioUserComplitedBossOrderStatisticGetParams;
import com.kaihei.esportingplus.trade.api.params.StudioUserOrderStatisticsQueryParams;
import com.kaihei.esportingplus.trade.api.params.UpdateOrderStatusRPGParams;
import com.kaihei.esportingplus.trade.api.vo.BaojiOrderListVo;
import com.kaihei.esportingplus.trade.api.vo.BossOrderListVo;
import com.kaihei.esportingplus.trade.api.vo.CreatedBossOrderVO;
import com.kaihei.esportingplus.trade.api.vo.OrderItemTeamVo;
import com.kaihei.esportingplus.trade.api.vo.OrderVO;
import com.kaihei.esportingplus.trade.api.vo.PVPPreIncomeVo;
import com.kaihei.esportingplus.trade.api.vo.StudioOrderListVo;
import com.kaihei.esportingplus.trade.api.vo.StudioUserOrderStatisticVO;
import com.kaihei.esportingplus.trade.api.vo.TeamOrderVo;
import feign.hystrix.FallbackFactory;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RPGOrdersClientFallbackFactory implements FallbackFactory<RPGOrdersServiceClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RPGOrdersClientFallbackFactory.class);

    @Override
    public RPGOrdersServiceClient create(Throwable throwable) {
        return new RPGOrdersServiceClient() {
            @Override
            public ResponsePacket<PagingResponse<BossOrderListVo>> getUserBossOrdersByPage(String token,OrderParams orderParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PagingResponse<BaojiOrderListVo>> getUserBaojiOrdersByPage(String token,OrderParams orderParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<CreatedBossOrderVO> createOrder(String token, OrderQueryParams orderQueryParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PagingResponse<StudioOrderListVo>> getStudioUserOrdersByPage(
                    StudioOrderQueryParams studioOrderQueryParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<String>> createBaojiOrder(String sequeue) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<OrderItemTeamVo> selectOrderItemTeamByOrderSequeue(
                    String sequeue) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<OrderVO> getBySequenceId(String sequeue) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<OrderVO>> getByTeamSequenceIdAndUids(String sequeue,
                    List<String> uids) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> updateOrderStatus(
                    UpdateOrderStatusRPGParams updateOrderStatusRPGParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> checkTeamMemberPayed(
                    CheckTeamMemberPayedParams checkTeamMemberPayedParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> checkTeamMemberPayed(String sequence) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PVPPreIncomeVo> getPreProfit(RPGInComeParams inComeParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<TeamOrderVo> getOrderDetailsBySequence(String token,
                    String sequence) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<StudioUserOrderStatisticVO>> getStudioUserOrderStatistics(
                    StudioUserOrderStatisticsQueryParams studioUserOrderStatisticsQueryParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Map<String, Long>> getTeamComplitedBossOrderStatistics(
                    List<String> teamSequences) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Map<String, Long>> getStudioUserComplitedBossOrderStatistics(
                    List<StudioUserComplitedBossOrderStatisticGetParams> studioUserComplitedBossOrderStatisticGetParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<TeamOrderVo> getOrderDetailsByUidAndTeamSequence(String token,
                    String teamSequence) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

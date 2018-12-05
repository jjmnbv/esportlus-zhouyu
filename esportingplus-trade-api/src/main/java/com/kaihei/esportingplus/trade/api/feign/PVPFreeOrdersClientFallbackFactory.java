package com.kaihei.esportingplus.trade.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.trade.api.params.ChickenPointIncomeParams;
import com.kaihei.esportingplus.trade.api.params.PVPFreeOrdersForBackGroundParams;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeBossOrderForBackGroundVO;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeOrderIncomeForBackGroundVO;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeOrderListVO;
import com.kaihei.esportingplus.trade.api.vo.PVPFreePreIncomeVo;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeTeamOrderVo;
import feign.hystrix.FallbackFactory;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PVPFreeOrdersClientFallbackFactory implements FallbackFactory<PVPFreeOrdersServiceClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PVPFreeOrdersClientFallbackFactory.class);

    @Override
    public PVPFreeOrdersServiceClient create(Throwable throwable) {
        return new PVPFreeOrdersServiceClient() {

            @Override
            public ResponsePacket<PVPFreePreIncomeVo> getChickenPointIncome(ChickenPointIncomeParams incomeParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PVPFreeTeamOrderVo> getOrderDetailsBySequence(String token,String sequence) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PVPFreeTeamOrderVo> getOrderDetailsByTeamSequence(String token,
                    String teamSequence) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PagingResponse<PVPFreeOrderListVO>> selectUserBossOrdersByPage(
                    String token,
                    Integer offset, Integer limit) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PagingResponse<PVPFreeOrderListVO>> selectUserBaojiOrdersByPage(
                    String token,
                    Integer offset, Integer limit) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PagingResponse<PVPFreeBossOrderForBackGroundVO>> selectPvpFreeBaojiOrderForBackGroundVO(
                    PVPFreeOrdersForBackGroundParams params) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PagingResponse<PVPFreeBossOrderForBackGroundVO>> selectPvpFreeBossOrderForBackGroundVO(
                    PVPFreeOrdersForBackGroundParams params) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<PVPFreeOrderIncomeForBackGroundVO>> selectPVPFreeBaojiFromBossIncome(
                    String sequence) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<PVPFreeOrderIncomeForBackGroundVO>> selectPVPFreeBossGiveBaojiIncome(
                    String sequence) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

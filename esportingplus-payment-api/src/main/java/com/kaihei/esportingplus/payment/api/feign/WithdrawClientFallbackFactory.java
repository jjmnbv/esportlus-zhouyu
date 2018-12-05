package com.kaihei.esportingplus.payment.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.payment.api.params.WithdrawCreateParams;
import com.kaihei.esportingplus.payment.api.params.WithdrawUpdateParams;
import com.kaihei.esportingplus.payment.api.vo.PageInfo;
import com.kaihei.esportingplus.payment.api.vo.WithdrawAuditListVo;
import com.kaihei.esportingplus.payment.api.vo.WithdrawConfigVo;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
public class WithdrawClientFallbackFactory implements FallbackFactory<WithdrawServiceClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WithdrawClientFallbackFactory.class);

    @Override
    public WithdrawServiceClient create(Throwable throwable) {
        return new WithdrawServiceClient() {

            @Override
            public ResponsePacket<Map<String, String>> createWithdrawOrder(String type, WithdrawCreateParams withdrawCreateParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Map<String, String>> updateWithdrawStatus(String type, WithdrawUpdateParams withdrawUpdateParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket getStarLightValues(String moneyType, String userIds) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Map<String, String>> convertStarlightToGCoin(String token, String amount, String sourceId, String currencyType) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket getWithdrawStatus(String outTradeNo, String userId, String orderId) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket getExchangeStatus(String orderId, String userId) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket convertScoreToStarlight(String userId, int amount, String outTradeNo) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Map<String, String>> createWithdrawAuditOrder(String token, Integer amount, String appId, Integer transferType, HttpServletRequest httpServletRequest) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PageInfo<WithdrawAuditListVo>> getAuditListByApp(String token, String page, String size) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PageInfo<WithdrawAuditListVo>> getAuditListByBackend(String uid, String verifyState, String orderId, String blockState, String page, String size) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket updateAuditState(WithdrawAuditListVo queryVo) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket updateBlockState(WithdrawAuditListVo queryVo) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<WithdrawConfigVo> getWithdrawConfigVo() {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket updateWithdrawConfig(WithdrawConfigVo configVo) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

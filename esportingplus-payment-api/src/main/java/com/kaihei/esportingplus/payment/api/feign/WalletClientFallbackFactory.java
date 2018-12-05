package com.kaihei.esportingplus.payment.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.payment.api.params.ConsumeGCoinParams;
import com.kaihei.esportingplus.payment.api.vo.WalletBillsVO;
import com.kaihei.esportingplus.payment.api.vo.WalletsVO;
import feign.hystrix.FallbackFactory;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 打赏服务熔断
 *
 * @author xiaolijun
 */
@Component
public class WalletClientFallbackFactory implements FallbackFactory<WalletServiceClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WalletClientFallbackFactory.class);

    @Override
    public WalletServiceClient create(Throwable throwable) {
        return new WalletServiceClient() {

            @Override
            public ResponsePacket consumeGCoin(String userId, String orderId) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Map<String, String>> createRewardOrder(String userId,
                    ConsumeGCoinParams consumeGCoinParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<WalletsVO> getBalance(String userId) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<WalletBillsVO>> getBills(String userId, String moneyType,
                    String page, String size,
                    String paymentChannel, String orderDimension, String orderType) {
                return ResponsePacket.onHystrix();
            }
            @Override
            public ResponsePacket<Map<String, Integer>> getStarlight(String userId, String tradeType) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<WalletsVO> getBalanceByApp(String userId) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

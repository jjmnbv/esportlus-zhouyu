package com.kaihei.esportingplus.payment.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.payment.api.params.CloseOrCancelPayOrderParams;
import com.kaihei.esportingplus.payment.api.params.PayOrderParams;
import com.kaihei.esportingplus.payment.api.params.RefundOrderParams;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 暴鸡支付服务熔断
 *
 * @author haycco
 */
@Component
public class TradeClientFallbackFactory implements FallbackFactory<TradeServiceClient> {

    private static final Logger logger = LoggerFactory.getLogger(TradeClientFallbackFactory.class);

    @Override
    public TradeServiceClient create(Throwable throwable) {
        return new TradeServiceClient() {

            @Override
            public ResponsePacket<Map<String, String>> create(PayOrderParams payOrderParams, String appId, String channelTag) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket cancel(CloseOrCancelPayOrderParams closeOrCancelPayOrderParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket close(CloseOrCancelPayOrderParams closeOrCancelPayOrderParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket query(String outTradeNo, String orderType) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket refund(RefundOrderParams refundOrderParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket refundQuery(String outRefundNo) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public Object createTest(PayOrderParams payOrderParams, String appId, String channelTag) {
                return null;
            }

        };
    }
}

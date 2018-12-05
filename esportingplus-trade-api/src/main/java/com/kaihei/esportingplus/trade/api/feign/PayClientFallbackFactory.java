package com.kaihei.esportingplus.trade.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.payment.api.vo.ExternalPaymentOrderVo;
import com.kaihei.esportingplus.payment.api.vo.ExternalRefundOrderVo;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PayClientFallbackFactory implements FallbackFactory<PayClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayClientFallbackFactory.class);

    @Override
    public PayClient create(Throwable throwable) {
        return new PayClient() {

            @Override
            public ResponsePacket payNofity(ExternalPaymentOrderVo payPacket) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket refundNofity(ExternalRefundOrderVo refundPacket) {
                return ResponsePacket.onHystrix();
            }

        };
    }
}

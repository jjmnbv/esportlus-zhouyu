package com.kaihei.esportingplus.payment.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.payment.api.params.GCoinPaymentCreateParams;
import com.kaihei.esportingplus.payment.api.params.GCoinPaymentUpdateParams;
import com.kaihei.esportingplus.payment.api.vo.GCoinPaymentPreVo;
import com.kaihei.esportingplus.payment.api.vo.GCoinPaymentVo;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GCoinPaymentClientFallbackFactory implements FallbackFactory<GCoinPaymentServiceClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GCoinPaymentClientFallbackFactory.class);

    @Override
    public GCoinPaymentServiceClient create(Throwable throwable) {

        return new GCoinPaymentServiceClient() {
            @Override
            public ResponsePacket<GCoinPaymentPreVo> createPrePaymentInfo(GCoinPaymentCreateParams gcoinPaymentCreateParams) {
                return ResponsePacket.onHystrix();
            }


            @Override
            public ResponsePacket<GCoinPaymentVo> updatePaymentInfo(String orderId,  GCoinPaymentUpdateParams gcoinPaymentUpdateParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<GCoinPaymentVo> getPaymentInfo(String orderType, String orderId,String outTradeNo) {
                return ResponsePacket.onHystrix();
            }


        };
    }
}

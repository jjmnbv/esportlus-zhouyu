package com.kaihei.esportingplus.payment.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.payment.api.params.GCoinRechargeCreateParams;
import com.kaihei.esportingplus.payment.api.params.GCoinRechargeUpdateParams;
import com.kaihei.esportingplus.payment.api.vo.GCoinRechargePreVo;
import com.kaihei.esportingplus.payment.api.vo.GCoinRechargeVo;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @program: esportingplus
 * @description:
 * @author: xusisi
 * @create: 2018-09-25 10:17
 **/
@Component
public class GCoinRechargeClientFallbackFactory implements FallbackFactory<GCoinRechargeServiceClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GCoinRechargeClientFallbackFactory.class);

    @Override
    public GCoinRechargeServiceClient create(Throwable throwable) {

        return new GCoinRechargeServiceClient() {
            @Override
            public ResponsePacket<GCoinRechargePreVo> createOrderInfo(GCoinRechargeCreateParams rechargeCreateParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<GCoinRechargeVo> updateOrderInfo(String orderId, GCoinRechargeUpdateParams rechargeUpdateParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<GCoinRechargeVo> getRechargeDetail( String orderId, String userId) {
                return ResponsePacket.onHystrix();
            }


        };
    }
}

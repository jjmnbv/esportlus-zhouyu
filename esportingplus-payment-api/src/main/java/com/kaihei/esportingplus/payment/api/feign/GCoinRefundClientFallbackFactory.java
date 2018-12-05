package com.kaihei.esportingplus.payment.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.payment.api.params.GCoinRefundParams;
import com.kaihei.esportingplus.payment.api.vo.GCoinRefundPreVo;
import com.kaihei.esportingplus.payment.api.vo.GCoinRefundVo;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @program: esportingplus
 * @description:
 * @author: xusisi
 * @create: 2018-09-25 10:17
 **/
@Component
public class GCoinRefundClientFallbackFactory implements FallbackFactory<GCoinRefundServiceClient> {

    @Override
    public GCoinRefundServiceClient create(Throwable throwable) {

        return new GCoinRefundServiceClient() {

            @Override
            public ResponsePacket<GCoinRefundPreVo> createOrderInfo(GCoinRefundParams gcoinRefundParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<GCoinRefundVo> getRefundInfo(String orderId, String orderType, String outRefundNo) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

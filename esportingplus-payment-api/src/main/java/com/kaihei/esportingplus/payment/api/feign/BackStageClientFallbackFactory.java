package com.kaihei.esportingplus.payment.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.payment.api.params.DeductParams;
import com.kaihei.esportingplus.payment.api.params.GCoinBackStageRechargeParam;
import com.kaihei.esportingplus.payment.api.vo.DeductOrderVo;
import com.kaihei.esportingplus.payment.api.vo.FrontGCcoinRechargeVo;
import com.kaihei.esportingplus.payment.api.vo.GCoinRechargeVo;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @program: esportingplus
 * @description: 暴鸡币、暴击值后台操作服务
 * @author: xusisi
 * @create: 2018-10-09 16:44
 **/
@Component
public class BackStageClientFallbackFactory implements FallbackFactory<BackStageServiceClient> {


    private static final Logger logger = LoggerFactory.getLogger(BackStageClientFallbackFactory.class);

    @Override
    public BackStageServiceClient create(Throwable throwable) {
        return new BackStageServiceClient() {

            @Override
            public ResponsePacket<GCoinRechargeVo> createGcoinRecharge(GCoinBackStageRechargeParam gCoinBackStageRechargeParam) throws Exception {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<DeductOrderVo> createDeductOrder(DeductParams deductParams) throws Exception {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<FrontGCcoinRechargeVo> getGCoinRechargeList(String userId, String channel, String sourceId, String beginDate, String endDate, String page, String size) throws Exception {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Map<String, Object>> getStarlightExchangeList(String uid, String beginDate, String endDate, String page, String size) throws Exception {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

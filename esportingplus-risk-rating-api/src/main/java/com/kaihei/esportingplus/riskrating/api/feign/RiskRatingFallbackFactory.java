package com.kaihei.esportingplus.riskrating.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.riskrating.api.params.LoginParams;
import com.kaihei.esportingplus.riskrating.api.params.RechargeRiskParams;
import com.kaihei.esportingplus.riskrating.api.vo.RiskBaseResponse;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 风控服务熔断
 * @author chenzhenjun
 */
@Component
public class RiskRatingFallbackFactory implements FallbackFactory<RiskRatingServiceClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RiskRatingFallbackFactory.class);

    @Override
    public RiskRatingServiceClient create(Throwable throwable) {
        return new RiskRatingServiceClient() {

            @Override
            public ResponsePacket<String> checkLoginOrRegister(LoginParams loginParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<RiskBaseResponse> checkRechargeStatus(String token,
                    Integer amount,
                    String deviceNo) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

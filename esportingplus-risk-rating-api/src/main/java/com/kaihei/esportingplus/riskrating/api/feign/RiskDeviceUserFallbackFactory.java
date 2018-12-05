package com.kaihei.esportingplus.riskrating.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.riskrating.api.params.RiskDeviceUserLogQueryParams;
import com.kaihei.esportingplus.riskrating.api.params.RiskDeviceWhiteQueryParams;
import com.kaihei.esportingplus.riskrating.api.params.RiskDeviceWhiteUpdateParams;
import com.kaihei.esportingplus.riskrating.api.vo.RiskDeviceWhiteVo;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class RiskDeviceUserFallbackFactory implements FallbackFactory<RiskDeviceUserClient> {

    @Override
    public RiskDeviceUserClient create(Throwable throwable) {
        return  new RiskDeviceUserClient() {
            @Override
            public ResponsePacket<Object> findRiskDeviceRechargeLogByPage(
                    RiskDeviceUserLogQueryParams params) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Object> findRiskDeviceBindByPage(
                    RiskDeviceWhiteQueryParams params) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<RiskDeviceWhiteVo> findRiskDeviceWhiteVoById(Long id) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> saveRiskDeviceWhite(RiskDeviceWhiteUpdateParams params) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> updateRiskDeviceWhite(RiskDeviceWhiteUpdateParams params) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

package com.kaihei.esportingplus.trade.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.trade.api.params.PVPInComeParams;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PVPOrdersClientFallbackFactory implements FallbackFactory<PVPOrdersServiceClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PVPOrdersClientFallbackFactory.class);

    @Override
    public PVPOrdersServiceClient create(Throwable throwable) {
        return new PVPOrdersServiceClient() {

            @Override
            public ResponsePacket checkTeamMemberPayed(String orderSequence) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket preIncome(PVPInComeParams pvpInComeParams) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

package com.kaihei.esportingplus.riskrating.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.riskrating.api.params.UserRechargeFreezeParams;
import feign.hystrix.FallbackFactory;

public class RiskRatingFreezeFallbackFactory implements FallbackFactory<RiskRatingFreezeClient> {

    @Override
    public RiskRatingFreezeClient create(Throwable throwable) {
        return new RiskRatingFreezeClient() {
            @Override
            public ResponsePacket<Boolean> freezeUserRecharge(
                    UserRechargeFreezeParams userRechargeFreezeParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Boolean> unfreezeUserRecharge(String uid) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamChickenPointUseVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 免费车队-使用鸡分配置-兑换暴击值 fallback
 * @author liangyi
 */
@Component
public class FreeTeamChikenPointExchangeBaojiClientFallbackFactory implements FallbackFactory<FreeTeamChikenPointExchangeBaojiServiceClient> {

    private static final Logger logger = LoggerFactory
            .getLogger(FreeTeamChikenPointExchangeBaojiClientFallbackFactory.class);

    @Override
    public FreeTeamChikenPointExchangeBaojiServiceClient create(Throwable throwable) {

        return new FreeTeamChikenPointExchangeBaojiServiceClient() {

            @Override
            public ResponsePacket<Void> addFreeTeamChikenPointUse(
                    FreeTeamChickenPointUseVO freeTeamChickenPointUseVO) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> updateFreeTeamChikenPointUse(
                    FreeTeamChickenPointUseVO freeTeamChickenPointUseVO) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> updateFreeTeamChikenPointUse(Long id) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<FreeTeamChickenPointUseVO> getFreeTeamChikenPointUse(Long id) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<FreeTeamChickenPointUseVO> getFreeTeamChikenPointUse(
                    Integer exchange_type) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

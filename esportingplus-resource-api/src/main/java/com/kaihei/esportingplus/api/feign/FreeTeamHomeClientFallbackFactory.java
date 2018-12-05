package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamAdvertiseHomeVo;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamHomeVo;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamScrollTemplateHomeVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import feign.hystrix.FallbackFactory;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class FreeTeamHomeClientFallbackFactory implements
        FallbackFactory<FreeTeamHomeServiceClient> {

    @Override
    public FreeTeamHomeServiceClient create(Throwable throwable) {
        return new FreeTeamHomeServiceClient() {


            @Override
            public ResponsePacket<List<FreeTeamAdvertiseHomeVo>> findAllFreeTeamAdvertise() {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> saveFreeTeamAdvertiseHome(
                    FreeTeamAdvertiseHomeVo freeTeamAdvertiseHomeVo) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> updateFreeTeamAdvertiseHome(
                    FreeTeamAdvertiseHomeVo freeTeamAdvertiseHomeVo) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> saveFreeTeamScrollTemplate(
                    FreeTeamScrollTemplateHomeVo freeTeamScrollTemplateHomeVo) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> updateFreeTeamScrollTemplate(
                    FreeTeamScrollTemplateHomeVo freeTeamScrollTemplateHomeVo) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> deleteFreeTeamScrollTemplate(Integer id) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<FreeTeamAdvertiseHomeVo> findFreeTeamAdvertiseForApp(String token,
                    Integer machineType) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<FreeTeamAdvertiseHomeVo> findFreeTeamAdvertise(
                    Integer machineType) {
                return ResponsePacket.onHystrix();
            }

        };
    }
}

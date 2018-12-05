package com.kaihei.esportingplus.riskrating.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.riskrating.api.params.FreeTeamBasicParams;
import com.kaihei.esportingplus.riskrating.api.params.FreeTeamCheckParams;
import com.kaihei.esportingplus.riskrating.api.vo.FreeTeamConfigVo;
import com.kaihei.esportingplus.riskrating.api.vo.FreeTeamResponse;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FreeTeamFallbackFactory implements FallbackFactory<FreeTeamClient> {

    @Override
    public FreeTeamClient create(Throwable throwable) {
        return  new FreeTeamClient() {
            @Override
            public ResponsePacket<FreeTeamResponse> checkUserRegister(FreeTeamBasicParams freeTeamBasicParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<FreeTeamResponse> checkFreeTeamChance(FreeTeamBasicParams freeTeamBasicParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket updateChanceTimes(String uids) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket insertWhiteList(String deviceId) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Map<String, Object>> getWhiteList(String deviceId, String beginDate, String endDate, String page, String size) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Boolean> deleteWhite(long id) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<FreeTeamConfigVo> getFreeTeamConfig() {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket updateFreeTeamConfig(FreeTeamConfigVo configVo) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

package com.kaihei.esportingplus.gamingteam.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.gamingteam.api.params.WxTeamEndParams;
import com.kaihei.esportingplus.gamingteam.api.params.WxTeamOrderCancelParams;
import com.kaihei.esportingplus.gamingteam.api.params.WxTeamStartParams;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class WxSmallProgramClientFallbackFactory implements FallbackFactory<WxSmallProgramPushClient> {

    @Override
    public WxSmallProgramPushClient create(Throwable throwable) {
        return new WxSmallProgramPushClient() {
            @Override
            public ResponsePacket<Void> pushOrderEnd(WxTeamEndParams pushParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> pushOrderCancel(WxTeamOrderCancelParams cancelParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> pushTeamStart(WxTeamStartParams startParams) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

package com.kaihei.esportingplus.marketing.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.marketing.api.event.TeamFreeEvent;
import com.kaihei.esportingplus.marketing.api.event.TeamFreeObtainstarEvent;
import feign.hystrix.FallbackFactory;

/**
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/11 11:09
 */
public class UserTaskFreeTeamServiceClientFallbackFactory implements
        FallbackFactory<UserTaskFreeTeamServiceClient> {

    @Override
    public UserTaskFreeTeamServiceClient create(Throwable throwable) {
        return new UserTaskFreeTeamServiceClient() {
            @Override
            public ResponsePacket<Void> finish(TeamFreeEvent params) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> obtainstar(TeamFreeObtainstarEvent params) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

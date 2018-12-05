package com.kaihei.esportingplus.gamingteam.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.gamingteam.api.params.ImDismissGroupParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImEndTeamMsgParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImFullMsgParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupCommonParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupJoinParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupLeavelParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImMatchParams;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ImMsgServiceFallbackFactory implements FallbackFactory<ImMsgServiceClient> {

    @Override
    public ImMsgServiceClient create(Throwable throwable) {
        return new ImMsgServiceClient() {

            @Override
            public ResponsePacket<Boolean> createGroup(ImGroupCommonParams params) {
                ResponsePacket packet = ResponsePacket.onHystrix();
                packet.setData(false);
                return packet;
            }

            @Override
            public ResponsePacket<Boolean> joinGroup(ImGroupJoinParams params) {
                ResponsePacket packet = ResponsePacket.onHystrix();
                packet.setData(false);
                return packet;
            }

            @Override
            public ResponsePacket<Void> leaveGroup(ImGroupLeavelParams params) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> sendFullMembersMsg(ImFullMsgParams params) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> endTeam(ImEndTeamMsgParams params) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> dismissGroup(ImDismissGroupParams params) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> sendMatchMsg(ImMatchParams params) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

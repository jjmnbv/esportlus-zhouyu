package com.kaihei.esportingplus.gamingteam.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.gamingteam.api.params.TeamImCmdMsgBaseParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupBaseParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupCommonParams;
import com.kaihei.esportingplus.gamingteam.api.params.TeamImCmdMsgOutMemberParams;
import com.kaihei.esportingplus.gamingteam.api.params.UserNameImCmdMsgParams;
import feign.hystrix.FallbackFactory;

public class ImServiceFallbackFactory implements FallbackFactory<ImServiceClient> {

    @Override
    public ImServiceClient create(Throwable throwable) {
       return  new ImServiceClient(){

           @Override
           public ResponsePacket<Void> createGroup(ImGroupCommonParams params) {
               return ResponsePacket.onHystrix();
           }

           @Override
           public ResponsePacket<Void> joinGroup(ImGroupCommonParams params) {
               return ResponsePacket.onHystrix();
           }

           @Override
           public ResponsePacket<Void> leaveGroup(ImGroupBaseParams params) {
               return ResponsePacket.onHystrix();
           }

           @Override
           public ResponsePacket<Void> dismissGroup(ImGroupBaseParams params) {
               return ResponsePacket.onHystrix();
           }

           @Override
           public ResponsePacket<Void> startTeam(TeamImCmdMsgBaseParams params) {
               return ResponsePacket.onHystrix();
           }

           @Override
           public ResponsePacket<Void> outTeam(TeamImCmdMsgOutMemberParams params) {
               return ResponsePacket.onHystrix();
           }

           @Override
           public ResponsePacket<Void> dismissTeam(TeamImCmdMsgBaseParams params) {
               return ResponsePacket.onHystrix();
           }

           @Override
           public ResponsePacket<Void> changeTeamCount(TeamImCmdMsgBaseParams params) {
               return ResponsePacket.onHystrix();
           }

           @Override
           public ResponsePacket<Void> endTeamServer(TeamImCmdMsgBaseParams params) {
               return ResponsePacket.onHystrix();
           }

           @Override
           public ResponsePacket<Void> paidFinish(TeamImCmdMsgBaseParams params) {
               return ResponsePacket.onHystrix();
           }

           @Override
           public ResponsePacket<Void> confirmInTeam(TeamImCmdMsgBaseParams params) {
               return ResponsePacket.onHystrix();
           }

           @Override
           public ResponsePacket<Void> joinTeam(UserNameImCmdMsgParams params) {
               return ResponsePacket.onHystrix();
           }

           @Override
           public ResponsePacket<Void> exitTeam(UserNameImCmdMsgParams params) {
               return ResponsePacket.onHystrix();
           }


       };
    }
}

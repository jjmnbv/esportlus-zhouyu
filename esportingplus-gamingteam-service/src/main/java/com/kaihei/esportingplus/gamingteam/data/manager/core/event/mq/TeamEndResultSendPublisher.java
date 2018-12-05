//package com.kaihei.esportingplus.gamingteam.data.manager.core.event.mq;//package com.kaihei.esportingplus.gamingteam.data.manager.pvp.event.mq;
//
//import com.kaihei.esportingplus.common.constant.RocketMQConstant;
//import com.kaihei.esportingplus.gamingteam.api.mq.PVPFreeTeamEndOrderMessage;
//import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
//import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
//import com.kaihei.esportingplus.gamingteam.data.manager.core.producer.TeamEndGameResultSendProducer;
//import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.EndTeamScene;
//import com.kaihei.esportingplus.gamingteam.data.manager.core.team.EventPublisher;
//import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
//import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
//import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
//import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGameResult;
//import com.maihaoche.starter.mq.base.MessageBuilder;
//import java.util.List;
//import org.apache.rocketmq.common.message.Message;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * 在游戏结束时、推送MQ消息
// */
//@Component
//@OnScene(includes = {EndTeamScene.class})
//public class TeamEndResultSendPublisher extends EventPublisher<TeamGame> {
//
//    private final String topic = RocketMQConstant.TOPIC_PVP_FREE;
//    private final String tag = RocketMQConstant.UPDATE_ORDER_STATUS_TAG;
//    @Autowired
//    private TeamEndGameResultSendProducer teamEndGameResultSendProducer;
//
//
//    @Override
//    protected void doPublish() {
//        PVPContext<TeamGame, TeamMember> context = pvpContextHolder.getContext();
//        List<TeamGameResult> teamGameResults = context.getTeamGameResults();
//        Team team = context.getTeam();
//
//        teamGameResults.forEach(teamGameResult -> {
//            PVPFreeTeamEndOrderMessage params = PVPFreeTeamEndOrderMessage.builder()
//                    .gameResultCode(teamGameResult.getGameResult())
//                    .teamSequence(team.getSequence()).teamStatus(team.getStatus().intValue())
//                    .build();
//
//            Message message = MessageBuilder.of(params).topic(topic).tag(tag).build();
//
//            teamEndGameResultSendProducer.sendMessageInTransaction(message, team);
//        });
//    }
//}

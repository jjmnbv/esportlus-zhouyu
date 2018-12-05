//package com.kaihei.esportingplus.gamingteam.data.manager.core.event.mq;//package com.kaihei.esportingplus.gamingteam.data.manager.pvp.event.mq;
//
//import com.kaihei.esportingplus.common.constant.RocketMQConstant;
//import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
//import com.kaihei.esportingplus.common.exception.BusinessException;
//import com.kaihei.esportingplus.gamingteam.api.mq.PVPFreeTeamStartOrderMessage;
//import com.kaihei.esportingplus.gamingteam.api.vo.PVPFreeMemberInfoVO;
//import com.kaihei.esportingplus.gamingteam.api.vo.PVPFreeTeamStartOrderVO;
//import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
//import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
//import com.kaihei.esportingplus.gamingteam.data.manager.core.producer.TeamStartOrderCreateProducer;
//import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.StartTeamScene;
//import com.kaihei.esportingplus.gamingteam.data.manager.core.team.EventPublisher;
//import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
//import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
//import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
//import com.maihaoche.starter.mq.MQException;
//import com.maihaoche.starter.mq.base.MessageBuilder;
//import java.util.List;
//import java.util.stream.Collectors;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.rocketmq.client.producer.SendResult;
//import org.apache.rocketmq.client.producer.SendStatus;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * 在车队开始时推送MQ消息
// */
//@Slf4j
//@Component
//@OnScene(includes = StartTeamScene.class)
//public class TeamStartOrderCreatePublisher extends EventPublisher<TeamGame> {
//
//    private final String topic = RocketMQConstant.TOPIC_PVP_FREE;
//    private final String tag = RocketMQConstant.CREATE_BAOJI_ORDER_TAG;
//
//    @Autowired
//    private TeamStartOrderCreateProducer teamStartOrderCreateProducer;
//
//    @Override
//    protected void doPublish() {
//        PVPFreeTeamStartOrderMessage pvpFreeTeamStartOrderMessage = PVPFreeTeamStartOrderMessage
//                .builder()
//                .build();
//
//        //组装参数
//        PVPFreeTeamStartOrderVO pvpFreeTeamStartOrderVO = new PVPFreeTeamStartOrderVO();
//        pvpFreeTeamStartOrderMessage.setPvpTeamStartOrderVO(pvpFreeTeamStartOrderVO);
//
//        PVPContext<TeamGame, TeamMember> context = pvpContextHolder.getContext();
//        Team team = context.getTeam();
//        TeamGame teamGame = context.getTeamGame();
//
//        List<TeamMember> teamMemberList = context.getTeamMemberList();
//        pvpFreeTeamStartOrderVO.setGameCode(teamGame.getGameId());
//
//        team.cast(pvpFreeTeamStartOrderVO);
//        teamGame.cast(pvpFreeTeamStartOrderVO);
//
//        List<PVPFreeMemberInfoVO> pvpFreeMemberInfoVOS = teamMemberList.stream()
//                .map(tm -> {
//                    PVPFreeMemberInfoVO pvpFreeMemberInfoVO = tm.cast(PVPFreeMemberInfoVO.class);
//                    pvpFreeMemberInfoVO.setUserNickname(tm.getUsername());
//                    pvpFreeMemberInfoVO.setUserChickenId(tm.getChickenId());
//                    pvpFreeMemberInfoVO.setUserIdentity(tm.getUserIdentity().intValue());
//                    pvpFreeMemberInfoVO.setStatus(tm.getStatus().intValue());
//                    return pvpFreeMemberInfoVO;
//                })
//                .collect(Collectors.toList());
//
//        pvpFreeTeamStartOrderVO.setMemberInfos(pvpFreeMemberInfoVOS);
//
//        try {
//            SendResult sendResult = teamStartOrderCreateProducer.sendMessageInTransaction(
//                    MessageBuilder.of(pvpFreeTeamStartOrderMessage).topic(topic).tag(tag).build(),
//                    team);
//            //MQ发送失败
//            if (sendResult == null || !SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
//                throw new BusinessException(BizExceptionEnum.TEAM_MQ_SEND_FAIL);
//            }
//        } catch (MQException e) {
//            log.error("MQ消息发送失败", e);
//            throw new BusinessException(BizExceptionEnum.TEAM_MQ_SEND_FAIL);
//        }
//    }
//
//}

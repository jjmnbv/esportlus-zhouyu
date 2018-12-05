package com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.mq;

import com.alibaba.fastjson.JSON;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.gamingteam.api.enums.TeamStatusEnum;
import com.kaihei.esportingplus.gamingteam.api.mq.PVPFreeTeamEndOrderMessage;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamMemberVO;
import com.kaihei.esportingplus.gamingteam.data.manager.cache.PVPTeamCacheManager;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.mq.MQTransactionMsgGenerator;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGamePVPFree;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGameResult;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamMemberPVPFree;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PVPTeamFreeResultSendMsgGenerator implements MQTransactionMsgGenerator {

    @Autowired
    private PVPContextHolder<TeamGamePVPFree, TeamMemberPVPFree> pvpContextHolder;
    @Autowired
    private PVPTeamCacheManager pvpTeamCacheManager;

    @Override
    public Object generateMsg() {
        PVPContext<TeamGamePVPFree, TeamMemberPVPFree> context = pvpContextHolder.getContext();
        List<TeamGameResult> teamGameResults = context.getTeamGameResults();
        Team team = context.getTeam();
        TeamGamePVPFree teamGame = context.getTeamGame();
        List<TeamMemberPVPFree> teamMemberList = context.getTeamMemberList();

        List<TeamMemberVO> teamMemberVOS = teamMemberList.stream()
                .map(it -> it.cast(TeamMemberVO.class))
                .collect(Collectors.toList());

        TeamGameResult teamGameResult = teamGameResults.get(0);

        return PVPFreeTeamEndOrderMessage.builder()
                .gameResultCode(teamGameResult.getGameResult())
                .gameId(teamGame.getGameId())
                .teamSequence(team.getSequence()).teamStatus(team.getStatus().intValue())
                .roomNum(team.getRoomNum())
                .teamMemberVOS(teamMemberVOS)
                .build();
    }
    
    @Override
    public LocalTransactionState checkTransaction(MessageExt msg) {
        PVPFreeTeamEndOrderMessage pvpFreeTeamEndOrderMessage = JSON
                .parseObject(new String(msg.getBody()), PVPFreeTeamEndOrderMessage.class);

        Team realTeam = pvpTeamCacheManager.getTeam(pvpFreeTeamEndOrderMessage.getTeamSequence());
        TeamStatusEnum teamStatusEnum = TeamStatusEnum.of(realTeam.getStatus());
        if (!TeamStatusEnum.COMPLETED.equals(teamStatusEnum)) {
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        return LocalTransactionState.COMMIT_MESSAGE;
    }

    @Override
    public String getTopic() {
        return RocketMQConstant.TOPIC_PVP_FREE;
    }

    @Override
    public String getTag() {
        return RocketMQConstant.UPDATE_ORDER_STATUS_TAG;
    }
}

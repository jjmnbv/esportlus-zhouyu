package com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.mq;

import com.alibaba.fastjson.JSON;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.gamingteam.api.enums.TeamStatusEnum;
import com.kaihei.esportingplus.gamingteam.api.mq.PVPFreeTeamStartOrderMessage;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeMemberInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeTeamStartOrderVO;
import com.kaihei.esportingplus.gamingteam.data.manager.cache.PVPTeamCacheManager;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.mq.MQTransactionMsgGenerator;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGamePVPFree;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamMemberPVPFree;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TeamStartMsgGenerator implements MQTransactionMsgGenerator {

    @Autowired
    private PVPContextHolder<TeamGamePVPFree, TeamMemberPVPFree> pvpContextHolder;
    @Autowired
    private PVPTeamCacheManager pvpTeamCacheManager;

    @Override
    public Object generateMsg() {
        PVPFreeTeamStartOrderMessage pvpFreeTeamStartOrderMessage = PVPFreeTeamStartOrderMessage
                .builder()
                .build();

        //组装参数
        PVPFreeTeamStartOrderVO pvpFreeTeamStartOrderVO = new PVPFreeTeamStartOrderVO();
        pvpFreeTeamStartOrderMessage.setPvpTeamStartOrderVO(pvpFreeTeamStartOrderVO);

        PVPContext<TeamGamePVPFree, TeamMemberPVPFree> context = pvpContextHolder.getContext();
        Team team = context.getTeam();
        TeamGame teamGame = context.getTeamGame();

        List<TeamMemberPVPFree> teamMemberList = context.getTeamMemberList();
        pvpFreeTeamStartOrderVO.setGameCode(teamGame.getGameId());

        team.cast(pvpFreeTeamStartOrderVO);
        teamGame.cast(pvpFreeTeamStartOrderVO);

        List<PVPFreeMemberInfoVO> pvpFreeMemberInfoVOS = teamMemberList.stream()
                .map(tm -> {
                    PVPFreeMemberInfoVO pvpFreeMemberInfoVO = tm.cast(PVPFreeMemberInfoVO.class);
                    pvpFreeMemberInfoVO.setUserNickname(tm.getUsername());
                    pvpFreeMemberInfoVO.setUserChickenId(tm.getChickenId());
                    pvpFreeMemberInfoVO.setUserIdentity(tm.getUserIdentity().intValue());
                    pvpFreeMemberInfoVO.setStatus(tm.getStatus().intValue());
                    return pvpFreeMemberInfoVO;
                })
                .collect(Collectors.toList());

        pvpFreeTeamStartOrderVO.setMemberInfos(pvpFreeMemberInfoVOS);

        return pvpFreeTeamStartOrderMessage;
    }

    @Override
    public LocalTransactionState checkTransaction(MessageExt msg) {
        PVPFreeTeamStartOrderMessage pvpFreeTeamStartOrderMessage = JSON
                .parseObject(new String(msg.getBody()), PVPFreeTeamStartOrderMessage.class);
        String sequence = pvpFreeTeamStartOrderMessage.getPvpTeamStartOrderVO().getSequence();
        Team team = pvpTeamCacheManager.getTeam(sequence);
        if (team.getStatus().intValue() < TeamStatusEnum.RUNNING.getCode()) {
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
        return RocketMQConstant.CREATE_BAOJI_ORDER_TAG;
    }
}

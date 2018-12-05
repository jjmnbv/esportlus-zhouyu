package com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.mq;

import com.alibaba.fastjson.JSON;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamMemberVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPTeamLeaveMemberAfterStartedVO;
import com.kaihei.esportingplus.gamingteam.data.manager.cache.PVPTeamMemberCacheManager;
import com.kaihei.esportingplus.gamingteam.data.manager.core.condition.TeamMemberLeaveAfterStartedCondition;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.mq.MQTransactionMsgGenerator;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PVPTeamFreeQuitTeamAfterRunningMsgGenerator implements MQTransactionMsgGenerator {

    @Autowired
    private PVPTeamMemberCacheManager pvpTeamMemberCacheManager;

    @Autowired
    private PVPContextHolder<TeamGame, TeamMember> pvpContextHolder;


    @Autowired
    private TeamMemberLeaveAfterStartedCondition teamMemberLeaveAfterStartedCondition;


    @Override
    public String getTopic() {
        return RocketMQConstant.TOPIC_PVP_FREE;
    }

    @Override
    public String getTag() {
        return RocketMQConstant.QUIT_TEAM_AFTER_RUNNING;
    }

    @Override
    public Object generateMsg() {
        if (!teamMemberLeaveAfterStartedCondition.condition()) {
            return null;
        }
        PVPContext<TeamGame, TeamMember> context = pvpContextHolder.getContext();
        Team contextTeam = context.getTeam();
        TeamGame teamGame = context.getTeamGame();
        UserSessionContext user = pvpContextHolder.getUser();

        TeamMember leave = context.getTeamMemberList().stream()
                .filter(tm -> tm.getUid().equals(user.getUid())).findFirst().orElse(null);

        if (leave == null) {
            throw new BusinessException(BizExceptionEnum.USER_NOT_IN_CURRENT_TEAM);
        }

        TeamMemberVO teamMemberVO = TeamMemberVO.builder().baojiLevel(leave.getBaojiLevel())
                .uid(leave.getUid())
                .userIdentity(leave.getUserIdentity().intValue()).build();

        return PVPTeamLeaveMemberAfterStartedVO.builder().teamMemberVO(teamMemberVO)
                .teamSequence(contextTeam.getSequence()).userStatus(leave.getStatus().intValue())
                .gameId(teamGame.getGameId())
                .roomNum(contextTeam.getRoomNum())
                .build();
    }

    @Override
    public LocalTransactionState checkTransaction(MessageExt messageExt) {
        PVPTeamLeaveMemberAfterStartedVO pvpTeamLeaveMemberAfterStartedVO = JSON
                .parseObject(new String(messageExt.getBody()),
                        PVPTeamLeaveMemberAfterStartedVO.class);

        if (!pvpTeamMemberCacheManager
                .existsTeamMember(pvpTeamLeaveMemberAfterStartedVO.getTeamSequence(),
                        pvpTeamLeaveMemberAfterStartedVO.getTeamMemberVO().getUid())) {

            return LocalTransactionState.COMMIT_MESSAGE;
        }

        return LocalTransactionState.ROLLBACK_MESSAGE;
    }
}

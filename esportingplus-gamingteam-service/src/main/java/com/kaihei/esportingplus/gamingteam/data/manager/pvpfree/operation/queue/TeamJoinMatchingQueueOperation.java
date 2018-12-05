package com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.operation.queue;

import com.kaihei.esportingplus.common.tools.DateUtil;
import com.kaihei.esportingplus.gamingteam.data.manager.cache.PVPTeamQueueManager;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnCondition;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnCondition.OnConditionLogic;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.condition.TeamAfterRunningCondition;
import com.kaihei.esportingplus.gamingteam.data.manager.core.condition.TeamMemberBecomeUnFulledCondition;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.PVPFreeTeamMatchingVO;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.CreateTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.after.AfterRestoreOperation;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGamePVPFree;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamMemberPVPFree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 车队创建、变回未满员，加入匹配队列
 */
@Slf4j
@Component
@OnScene(includes = CreateTeamScene.class)
@OnCondition(value = {TeamMemberBecomeUnFulledCondition.class}, excludes = {
        TeamAfterRunningCondition.class}, logic = OnConditionLogic.AND)
public class TeamJoinMatchingQueueOperation extends
        AfterRestoreOperation<TeamGamePVPFree> implements
        Ordered {

    @Autowired
    private PVPContextHolder<TeamGamePVPFree, TeamMemberPVPFree> pvpContextHolder;

    @Autowired
    private PVPTeamQueueManager pvpTeamQueueManager;

    @Override
    public void doOperate() {
        PVPContext<TeamGamePVPFree, TeamMemberPVPFree> context = pvpContextHolder.getContext();
        Team team = context.getTeam();
        TeamGamePVPFree teamGame = context.getTeamGame();

        PVPFreeTeamMatchingVO pvpFreeTeamMatchingVO = PVPFreeTeamMatchingVO.builder()
                .groupId(team.getGroupId())
                .teamSequence(team.getSequence())
                .freeMember(team.getActuallyPositionCount() - context.getTeamMemberList().size())
                .gameDanLowerLimit(teamGame.getLowerDanId())
                .gameDanTopLimit(teamGame.getUpperDanId())
                .teamCreateTime(DateUtil.dateToLocalDateTime(team.getGmtCreate()))
                .build();

        log.info("将车队放入匹配队列 -> {}", pvpFreeTeamMatchingVO);
        pvpTeamQueueManager
                .joinQueue(team.getSettlementType().intValue(), teamGame.getFreeTeamTypeId(),
                        teamGame.getGameZoneId(),
                pvpFreeTeamMatchingVO);
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}

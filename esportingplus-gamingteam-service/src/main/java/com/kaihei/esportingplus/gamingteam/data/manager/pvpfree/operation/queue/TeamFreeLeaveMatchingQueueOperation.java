package com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.operation.queue;

import com.kaihei.esportingplus.gamingteam.data.manager.cache.PVPTeamQueueManager;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnCondition;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.condition.TeamMemberBecomeFulledCondition;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.DismissTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.after.AfterOperation;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGamePVPFree;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamMemberPVPFree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 车队满员或解散、退出匹配队列
 */
@Component
@OnScene(includes = DismissTeamScene.class)
@OnCondition(TeamMemberBecomeFulledCondition.class)
public class TeamFreeLeaveMatchingQueueOperation extends AfterOperation<TeamGamePVPFree> {

    @Autowired
    private PVPContextHolder<TeamGamePVPFree, TeamMemberPVPFree> pvpContextHolder;

    @Autowired
    private PVPTeamQueueManager pvpTeamQueueManager;

    @Override
    public void doOperate() {
        PVPContext<TeamGamePVPFree, TeamMemberPVPFree> context = pvpContextHolder.getContext();
        Team team = context.getTeam();
        TeamGamePVPFree teamGame = context.getTeamGame();
        log.info("从车队匹配列表移除车队->{}", team.getSequence());
        pvpTeamQueueManager
                .quitQueue(team.getSettlementType().intValue(), teamGame.getFreeTeamTypeId(),
                        teamGame.getGameZoneId(),
                        team.getSequence());
    }

}

package com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.operation.queue;//package com.kaihei.esportingplus.gamingteam.data.manager.pvp.operation.before.sepecail.pvpfree.teamqueue;

import com.kaihei.esportingplus.common.tools.DateUtil;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnCondition;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.condition.BossLeaveBeforeTeamRunningCondition;
import com.kaihei.esportingplus.gamingteam.data.manager.core.condition.TeamMemberBecomeFulledCondition;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.PVPFreeTeamMatchingVO;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.JoinTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.KickOutTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.after.AfterOperation;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGamePVPFree;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamMemberPVPFree;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 老板进出车队、被踢时、更新队列信息
 */
@Component
@OnScene(includes = {JoinTeamScene.class, KickOutTeamScene.class})
@OnCondition(BossLeaveBeforeTeamRunningCondition.class)
public class TeamFreeMemberUpdateTeamQueueOperation extends
        AfterOperation<TeamGamePVPFree> implements Ordered {

    @Autowired
    private PVPContextHolder<TeamGamePVPFree, TeamMemberPVPFree> pvpContextHolder;
    @Autowired
    private TeamMemberBecomeFulledCondition teamMemberBecomeFulledCondition;
    @Autowired
    private TeamJoinMatchingQueueOperation teamJoinMatchingQueueOperation;

    @Override
    public void doOperate() {
        PVPContext<TeamGamePVPFree, TeamMemberPVPFree> context = pvpContextHolder.getContext();
        Team team = context.getTeam();
        TeamGamePVPFree teamGame = context.getTeamGame();
        List<TeamMemberPVPFree> teamMemberList = context.getTeamMemberList();

        PVPFreeTeamMatchingVO pvpFreeTeamMatchingVO = new PVPFreeTeamMatchingVO();
        pvpFreeTeamMatchingVO
                .setFreeMember(team.getActuallyPositionCount() - teamMemberList.size());
        pvpFreeTeamMatchingVO.setGameDanLowerLimit(teamGame.getLowerDanId());
        pvpFreeTeamMatchingVO.setGameDanTopLimit(teamGame.getUpperDanId());
        pvpFreeTeamMatchingVO.setTeamCreateTime(DateUtil.dateToLocalDateTime(team.getGmtCreate()));
        pvpFreeTeamMatchingVO.setTeamSequence(team.getSequence());
        pvpFreeTeamMatchingVO.setGroupId(team.getGroupId());

        log.info("更新匹配队列 -> {}", pvpFreeTeamMatchingVO);

        if (teamMemberBecomeFulledCondition.condition()) {
            log.info("满员了、不再执行匹配列表更新操作");
            return;
        }
        teamJoinMatchingQueueOperation.operate();
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE - 1;
    }
}

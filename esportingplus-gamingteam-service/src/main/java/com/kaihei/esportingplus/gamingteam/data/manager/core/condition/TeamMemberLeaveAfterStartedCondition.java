package com.kaihei.esportingplus.gamingteam.data.manager.core.condition;

import com.kaihei.esportingplus.gamingteam.api.enums.TeamStatusEnum;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.LeaveTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.Condition;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import org.springframework.stereotype.Component;

/**
 * 达成
 */
@Component
@OnScene(includes = {LeaveTeamScene.class})
public class TeamMemberLeaveAfterStartedCondition extends Condition<TeamGame> {

    /**
     * 由子类实现
     *
     * 判断是否满足条件
     */
    @Override
    protected boolean onCondition() {
        PVPContext<TeamGame, TeamMember> context = pvpContextHolder.getContext();
        Team contextTeam = context.getTeam();
        TeamStatusEnum running = TeamStatusEnum.RUNNING;
        boolean result = running.equals(TeamStatusEnum.of(contextTeam.getStatus()));

        if (result) {
            log.info("---->|达成 用户在开车后离开 条件");
        }

        return result;
    }
}

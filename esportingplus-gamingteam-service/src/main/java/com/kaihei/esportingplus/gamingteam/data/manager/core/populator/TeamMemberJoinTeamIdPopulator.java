package com.kaihei.esportingplus.gamingteam.data.manager.core.populator;

import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.JoinTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.UnnecessaryPopulator;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import java.util.Date;
import org.springframework.stereotype.Component;


@Component
@OnScene(includes = {JoinTeamScene.class})
public class TeamMemberJoinTeamIdPopulator extends UnnecessaryPopulator<TeamGame> {

    /**
     * 由子类实现
     *
     * 满足条件就做参数的填充操作
     */
    @Override
    protected void doPopulate() {
        PVPContext<TeamGame, TeamMember> context = pvpContextHolder.getContext();
        TeamMember join = context.getJoin();
        Team team = context.getTeam();
        Date now = new Date();

        TeamMember leader = context.getLeader();
        join.setTeamId(team.getId());
        join.setGmtModified(now);

        //队长顺便设置
        if (leader.getTeamId() == null) {
            leader.setTeamId(team.getId());
            leader.setGmtModified(now);
        }
    }
}

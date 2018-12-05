package com.kaihei.esportingplus.gamingteam.data.manager.core.condition;

import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.CreateTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.Condition;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import org.springframework.stereotype.Component;

@Component
@OnScene(excludes = CreateTeamScene.class)
public class TeamMemberBecomeFulledCondition extends Condition<TeamGame> {

    /**
     * 由子类实现
     *
     * 判断是否满足条件
     */
    @Override
    protected boolean onCondition() {
        PVPContext<TeamGame, TeamMember> backUp = pvpContextHolder.getBackup();
        PVPContext<TeamGame, TeamMember> context = pvpContextHolder.getContext();

        Integer bap = backUp.getTeam().getActuallyPositionCount();
        Integer cap = context.getTeam().getActuallyPositionCount();
        //修改前非满员、修改后满员
        boolean result =
                backUp.getTeamMemberList().size() < bap && cap == context.getTeamMemberList()
                        .size();
        if (result) {
            log.info("---->|达成 车队从未满员变成满员 条件");
        }
        return result;
    }
}

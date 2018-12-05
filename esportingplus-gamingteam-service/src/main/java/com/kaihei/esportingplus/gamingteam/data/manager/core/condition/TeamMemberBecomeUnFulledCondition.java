package com.kaihei.esportingplus.gamingteam.data.manager.core.condition;//package com.kaihei.esportingplus.gamingteam.data.manager.pvp.condition;

import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.Condition;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import org.springframework.stereotype.Component;

/**
 * 判断从其他状态 -> 未满员状态
 */
@Component
public class TeamMemberBecomeUnFulledCondition extends Condition<TeamGame> {

    /**
     * 由子类实现
     *
     * 判断是否满足条件
     */
    @Override
    protected boolean onCondition() {
        //改之前不是满的直接返回
        PVPContext<TeamGame, TeamMember> backUp = pvpContextHolder.getBackup();
        if (backUp.getTeam().getActuallyPositionCount() != backUp.getTeamMemberList().size()) {
            return false;
        }

        //判断当前车队位置大于实际人数
        PVPContext<TeamGame, TeamMember> context = pvpContextHolder.getContext();
        boolean result = context.getTeam().getActuallyPositionCount() > context
                .getTeamMemberList()
                .size();

        if (result) {
            log.info("---->|达成 从满员变成未满员 条件");
        }
        return result;
    }
}

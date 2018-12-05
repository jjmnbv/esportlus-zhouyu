package com.kaihei.esportingplus.gamingteam.data.manager.core.condition;//package com.kaihei.esportingplus.gamingteam.data.manager.pvp.condition;

import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.gamingteam.api.enums.PVPTeamMemberStatusEnum;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.Condition;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 判断从其他状态转变为 -> 满员、且全员处于准备状态
 */
@Component
public class TeamMemberBecomeFulledAndReadyCondition extends Condition<TeamGame> {

    /**
     * 由子类实现
     *
     * 判断是否满足条件
     */
    @Override
    protected boolean onCondition() {
        PVPContext<TeamGame, TeamMember> backUp = pvpContextHolder.getBackup();
        Team backUpTeam = backUp.getTeam();
        List<TeamMember> backupTeamMembers = backUp.getTeamMemberList();

        PVPContext<TeamGame, TeamMember> context = pvpContextHolder.getContext();
        Team contextTeam = context.getTeam();
        List<TeamMember> teamMemberList = context.getTeamMemberList();

        boolean allready = contextTeam.getActuallyPositionCount().equals(teamMemberList.size());
        //满员
        if (!allready) {
            return false;
        }

        //全员处于准备状态
        allready = teamMemberList.stream()
                .filter(it -> UserIdentityEnum.BOSS
                        .equals(UserIdentityEnum.of(it.getUserIdentity())))
                .allMatch(it -> PVPTeamMemberStatusEnum.PREPARE_READY
                        .equals(PVPTeamMemberStatusEnum.of(it.getStatus())));
        if (!allready) {
            return false;
        }
        boolean backupAllReady = backupTeamMembers.stream().filter(it -> UserIdentityEnum.BOSS
                .equals(UserIdentityEnum.of(it.getUserIdentity())))
                .allMatch(it -> PVPTeamMemberStatusEnum.PREPARE_READY
                        .equals(PVPTeamMemberStatusEnum.of(it.getStatus())));
        //backup处于全员准备状态
        if (backupAllReady && backUpTeam.getActuallyPositionCount()
                .equals(backupTeamMembers.size())) {
            return false;
        }
        log.info("---->|达成 全员准备状态 条件");
        return true;
    }
}

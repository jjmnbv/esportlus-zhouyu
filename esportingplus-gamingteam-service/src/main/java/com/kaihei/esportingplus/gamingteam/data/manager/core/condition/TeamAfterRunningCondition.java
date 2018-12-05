package com.kaihei.esportingplus.gamingteam.data.manager.core.condition;

import com.kaihei.esportingplus.gamingteam.api.enums.TeamStatusEnum;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.RecreateTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.Condition;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TeamAfterRunningCondition extends Condition<TeamGame> {

    @Autowired
    private RecreateTeamScene recreateTeamScene;
    /**
     * 由子类实现
     *
     * 判断是否满足条件
     */
    @Override
    protected boolean onCondition() {
        //重建车队false
        if (pvpContextHolder.getScene().equals(recreateTeamScene)) {
            return false;
        }

        PVPContext<TeamGame, TeamMember> backup = pvpContextHolder.getBackup();
        Team backupTeam = backup.getTeam();

        return backupTeam.getStatus() >= ((byte) (TeamStatusEnum.RUNNING.getCode()));
    }
}

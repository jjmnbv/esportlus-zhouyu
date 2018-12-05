package com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.operation;

import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.StartTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.after.AfterOperation;
import com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.job.PVPTeamAutoDismissJobOperation;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGamePVPFree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@OnScene(includes = StartTeamScene.class)
public class TeamFreeStartUndoAutoDismissJobOperation extends AfterOperation<TeamGamePVPFree> {

    @Autowired
    private PVPTeamAutoDismissJobOperation pvpTeamAutoDismissJobOperation;

    /**
     * 由子类实现
     *
     * 进行一些Service后续的操作
     */
    @Override
    protected void doOperate() {
        Team team = pvpContextHolder.getContext().getTeam();
        pvpTeamAutoDismissJobOperation.removeJob(team.getSequence());
    }
}

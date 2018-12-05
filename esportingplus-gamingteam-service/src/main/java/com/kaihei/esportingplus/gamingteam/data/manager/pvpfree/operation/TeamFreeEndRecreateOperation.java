package com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.operation;

import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.EndTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.after.AfterRestoreOperation;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGamePVPFree;
import com.kaihei.esportingplus.gamingteam.domain.service.PVPTeamFreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@OnScene(includes = {EndTeamScene.class})
public class TeamFreeEndRecreateOperation extends AfterRestoreOperation<TeamGamePVPFree> {

    @Lazy
    @Autowired
    private PVPTeamFreeService pvpTeamFreeService;

    /**
     * 由子类实现
     *
     * 进行一些Service后续的操作
     */
    @Override
    protected void doOperate() {
        pvpTeamFreeService.recreateTeam();
    }
}

package com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.populator;

import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnCondition;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.condition.BossLeaveBeforeTeamRunningCondition;
import com.kaihei.esportingplus.gamingteam.data.manager.core.populator.TeamGameDanNameParamPopulator;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.JoinTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.KickOutTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.UnnecessaryPopulator;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGamePVPFree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@OnScene(includes = {JoinTeamScene.class, KickOutTeamScene.class})
@OnCondition(value = {BossLeaveBeforeTeamRunningCondition.class})
public class TeamGamePVPFreeDanNameRePopulator extends UnnecessaryPopulator<TeamGamePVPFree> {

    @Autowired
    private TeamGameDanNameParamPopulator teamGameDanNameParamPopulator;

    /**
     * 由子类实现
     *
     * 满足条件就做参数的填充操作
     */
    @Override
    protected void doPopulate() {
        teamGameDanNameParamPopulator.populate();
    }
}

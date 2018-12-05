package com.kaihei.esportingplus.gamingteam.data.manager.core.condition;

import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.LeaveTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@OnScene(includes = LeaveTeamScene.class)
@Component
public class BossLeaveBeforeTeamRunningCondition extends Condition<TeamGame> {

    @Autowired
    private TeamBeforeRunningCondition teamBeforeRunningCondition;

    /**
     * 由子类实现
     *
     * 判断是否满足条件
     */
    @Override
    protected boolean onCondition() {
        return teamBeforeRunningCondition.condition();
    }
}

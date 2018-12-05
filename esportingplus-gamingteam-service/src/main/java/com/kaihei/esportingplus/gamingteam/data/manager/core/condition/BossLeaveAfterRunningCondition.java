package com.kaihei.esportingplus.gamingteam.data.manager.core.condition;

import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.LeaveTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@OnScene(includes = {LeaveTeamScene.class})
public class BossLeaveAfterRunningCondition extends Condition<TeamGame> {

    @Autowired
    private TeamAfterRunningCondition teamAfterRunningCondition;

    /**
     * 由子类实现
     *
     * 判断是否满足条件
     */
    @Override
    protected boolean onCondition() {
        return teamAfterRunningCondition.condition();
    }
}

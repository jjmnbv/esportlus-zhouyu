package com.kaihei.esportingplus.gamingteam.data.manager.core.validator.before;

import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.gamingteam.api.enums.TeamStatusEnum;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.BossCancelPrepareScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.BossConfirmPrepareScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.JoinTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.KickOutTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.StartTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.UpdateTeamPositionCountScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.before.BeforeValidator;
import org.springframework.stereotype.Component;

@Component
@OnScene(includes = {JoinTeamScene.class, BossConfirmPrepareScene.class,
        BossCancelPrepareScene.class, KickOutTeamScene.class, StartTeamScene.class,
        UpdateTeamPositionCountScene.class})
public class TeamIsStartedValidator extends BeforeValidator<TeamGame> {

    /**
     * 由子类实现
     *
     * 不满足条件抛 {@link BusinessException}
     */
    @Override
    protected void doValidate() {
        if (TeamStatusEnum
                .of(pvpContextHolder.getBackup().getTeam().getStatus())
                .equals(TeamStatusEnum.RUNNING)) {
            throw new BusinessException(BizExceptionEnum.TEAM_IS_RUNNING);
        }
    }
}

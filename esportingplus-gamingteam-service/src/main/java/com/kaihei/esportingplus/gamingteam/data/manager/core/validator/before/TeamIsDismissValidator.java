package com.kaihei.esportingplus.gamingteam.data.manager.core.validator.before;

import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.gamingteam.api.enums.TeamStatusEnum;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.BossCancelPrepareScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.BossConfirmPrepareScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.DismissTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.EndTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.JoinTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.KickOutTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.LeaveTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.StartTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.UpdateTeamPositionCountScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.before.BeforeValidator;
import org.springframework.stereotype.Component;

@Component
@OnScene(includes = {JoinTeamScene.class, LeaveTeamScene.class, KickOutTeamScene.class,
        BossConfirmPrepareScene.class, BossCancelPrepareScene.class, StartTeamScene.class,
        UpdateTeamPositionCountScene.class, DismissTeamScene.class, EndTeamScene.class})
public class TeamIsDismissValidator extends BeforeValidator<TeamGame> {

    /**
     * 由子类实现
     *
     * 不满足条件抛 {@link BusinessException}
     */
    @Override
    protected void doValidate() {
        if (TeamStatusEnum.DISMISSED
                .equals(TeamStatusEnum.of(pvpContextHolder.getBackup().getTeam().getStatus()))) {
            throw new BusinessException(BizExceptionEnum.TEAM_HAS_DISMISSED);
        }
    }
}

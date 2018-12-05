package com.kaihei.esportingplus.gamingteam.data.manager.core.validator.before;

import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.gamingteam.api.enums.PVPTeamMemberStatusEnum;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.BossCancelPrepareScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.BossConfirmPrepareScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.LeaveTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.before.BeforeValidator;
import org.springframework.stereotype.Component;

/**
 * 老板确认准备时，判断老板在车队里
 */
@Component
@OnScene(includes = {BossConfirmPrepareScene.class, BossCancelPrepareScene.class,
        LeaveTeamScene.class})
public class BossNotInTeamValidator extends BeforeValidator<TeamGame> {


    /**
     * 执行校验
     */
    @Override
    protected void doValidate() {
        TeamMember me = pvpContextHolder.getBackup().getMe();
        if (me == null || PVPTeamMemberStatusEnum.TEAM_START_QUIT
                .equals(PVPTeamMemberStatusEnum.of(me.getStatus()))) {
            throw new BusinessException(BizExceptionEnum.USER_NOT_IN_CURRENT_TEAM);
        }
    }
}

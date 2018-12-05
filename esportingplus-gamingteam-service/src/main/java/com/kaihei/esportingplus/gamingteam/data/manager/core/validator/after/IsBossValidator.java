package com.kaihei.esportingplus.gamingteam.data.manager.core.validator.after;//package com.kaihei.esportingplus.gamingteam.data.manager.pvp.validator.general;

import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.BossCancelPrepareScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.BossConfirmPrepareScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.JoinTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.LeaveTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.after.AfterValidator;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import org.springframework.stereotype.Component;

/**
 * 判断操作人是否是老板
 */
@Component
@OnScene(includes = {JoinTeamScene.class, LeaveTeamScene.class, BossCancelPrepareScene.class,
        BossConfirmPrepareScene.class})
public class IsBossValidator extends AfterValidator<TeamGame> {

    /**
     * 执行校验
     */
    @Override
    protected void doValidate() {
        TeamMember me = pvpContextHolder.getContext().getMe();
        if (me == null) {
            me = pvpContextHolder.getBackup().getMe();
        }
        if (me == null) {
            throw new BusinessException(BizExceptionEnum.USER_NOT_IN_CURRENT_TEAM);
        }
        //操作人不是老板
        if (!UserIdentityEnum.BOSS.equals(UserIdentityEnum.of(me.getUserIdentity()))) {
            throw new BusinessException(BizExceptionEnum.TEAM_UNSUPPORTED_OPERATION);
        }
    }
}

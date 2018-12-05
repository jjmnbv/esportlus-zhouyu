package com.kaihei.esportingplus.gamingteam.data.manager.core.validator.after;

import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.DismissTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.EndTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.KickOutTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.StartTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.UpdateTeamPositionCountScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.after.AfterValidator;
import org.springframework.stereotype.Component;

/**
 * 判断操作人是否是队长
 */
@Component
@OnScene(includes = {EndTeamScene.class, StartTeamScene.class, KickOutTeamScene.class,
        DismissTeamScene.class, UpdateTeamPositionCountScene.class})
public class IsLeaderValidator extends AfterValidator<TeamGame> {

    /**
     * 执行校验
     */
    @Override
    protected void doValidate() {
        PVPContext<TeamGame, TeamMember> context = pvpContextHolder.getContext();
        TeamMember me = context.getMe();
        TeamMember leader = context.getLeader();
        if (!leader.equals(me)) {
            throw new BusinessException(BizExceptionEnum.TEAM_NOT_LEADER);
        }
    }
}

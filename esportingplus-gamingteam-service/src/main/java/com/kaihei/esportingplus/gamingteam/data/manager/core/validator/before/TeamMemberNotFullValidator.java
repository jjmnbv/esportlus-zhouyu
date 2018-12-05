package com.kaihei.esportingplus.gamingteam.data.manager.core.validator.before;//package com.kaihei.esportingplus.gamingteam.data.manager.pvp.validator.general;

import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.StartTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.before.BeforeValidator;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 队长开车场景、前检查车队人员是否未满
 */
@Slf4j
@Component
@OnScene(includes = StartTeamScene.class)
public class TeamMemberNotFullValidator extends BeforeValidator<TeamGame> {

    /**
     * 执行校验
     */
    @Override
    protected void doValidate() {
        PVPContext<TeamGame, TeamMember> context = pvpContextHolder.getContext();
        Team contextTeam = context.getTeam();
        Integer actuallyPositionCount = contextTeam.getActuallyPositionCount();
        if (actuallyPositionCount > context.getTeamMemberList().size()) {
            throw new BusinessException(BizExceptionEnum.TEAM_MEMBER_NOT_FULL);
        }
    }
}

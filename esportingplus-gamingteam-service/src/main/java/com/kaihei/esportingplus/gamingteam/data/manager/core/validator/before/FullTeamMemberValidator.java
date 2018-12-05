package com.kaihei.esportingplus.gamingteam.data.manager.core.validator.before;

import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.JoinTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.before.BeforeValidator;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 老板加入车队场景，判断上否满员
 */
@Component
@OnScene(includes = {JoinTeamScene.class})
public class FullTeamMemberValidator extends BeforeValidator<TeamGame> {

    /**
     * 执行校验
     */
    @Override
    protected void doValidate() {
        PVPContext<TeamGame, TeamMember> backup = pvpContextHolder.getBackup();
        Team backupTeam = backup.getTeam();
        List<TeamMember> teamMemberList = backup.getTeamMemberList();
        if (teamMemberList.size() >= backupTeam.getActuallyPositionCount()) {
            throw new BusinessException(BizExceptionEnum.TEAM_MEMBER_IS_FULL);
        }

    }
}

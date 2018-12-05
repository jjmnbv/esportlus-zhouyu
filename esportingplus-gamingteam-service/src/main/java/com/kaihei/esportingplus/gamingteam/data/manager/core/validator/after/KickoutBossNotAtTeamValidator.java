package com.kaihei.esportingplus.gamingteam.data.manager.core.validator.after;

import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.KickOutTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.after.AfterValidator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
@OnScene(includes = {KickOutTeamScene.class})
public class KickoutBossNotAtTeamValidator extends AfterValidator<TeamGame> {

    /**
     * 由子类实现
     *
     * 不满足条件抛 {@link BusinessException}
     */
    @Override
    protected void doValidate() {
        List<TeamMember> leaves = pvpContextHolder.getContext().getLeave();
        if (leaves == null || leaves.isEmpty()) {
            throw new BusinessException(0, "用户已被踢出车队");
        }
    }
}

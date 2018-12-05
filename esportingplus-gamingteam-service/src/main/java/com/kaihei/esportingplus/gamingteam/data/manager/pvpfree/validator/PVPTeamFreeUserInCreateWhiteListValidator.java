package com.kaihei.esportingplus.gamingteam.data.manager.pvp.validator.specail;

import com.kaihei.esportingplus.api.feign.FreeTeamConfigServiceClient;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.CreateTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.after.AfterValidator;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGamePVPFree;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamMemberPVPFree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 队长组建车队时，判断队长在可组建免费车队的白名单里
 */
@Component
@OnScene(includes = {CreateTeamScene.class})
public class PVPTeamFreeUserInCreateWhiteListValidator extends
        AfterValidator<TeamGamePVPFree> {

    @Autowired
    private FreeTeamConfigServiceClient freeTeamConfigServiceClient;
    @Autowired
    private PVPContextHolder<TeamGamePVPFree, TeamMemberPVPFree> pvpContextHolder;

    /**
     * 执行校验
     */
    @Override
    protected void doValidate() {
        TeamMemberPVPFree creator = pvpContextHolder.getContext().getJoin();
        Boolean inWhiteList = freeTeamConfigServiceClient
                .checkUserInFreeTeamUserWhiteList(creator.getUid()).getData();
        if (!inWhiteList) {
            throw new BusinessException(BizExceptionEnum.USER_NOT_IN_WHITE_LIST);
        }
    }
}

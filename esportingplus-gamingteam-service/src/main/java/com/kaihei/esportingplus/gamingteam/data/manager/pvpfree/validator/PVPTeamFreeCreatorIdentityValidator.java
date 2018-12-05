package com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.validator;

import com.kaihei.esportingplus.api.feign.FreeTeamTypeServiceClient;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamTypeDetailVO;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.CreateTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.after.AfterValidator;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGamePVPFree;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamMemberPVPFree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 谢思勇
 *
 * 免费车队创建人身份验证
 */
@Component
@OnScene(includes = CreateTeamScene.class)
public class PVPTeamFreeCreatorIdentityValidator extends AfterValidator<TeamGamePVPFree> {

    @Autowired
    private FreeTeamTypeServiceClient freeTeamTypeServiceClient;

    @Autowired
    private PVPContextHolder<TeamGamePVPFree, TeamMemberPVPFree> pvpContextHolder;

    /**
     * 由子类实现
     *
     * 不满足条件抛 {@link BusinessException}
     */
    @Override
    protected void doValidate() {
        PVPContext<TeamGamePVPFree, TeamMemberPVPFree> context = pvpContextHolder.getContext();
        TeamMemberPVPFree join = context.getJoin();
        TeamGamePVPFree teamGame = context.getTeamGame();

        //车队类型
        FreeTeamTypeDetailVO typeDetailVO = freeTeamTypeServiceClient
                .getFreeTeamTypeById(teamGame.getFreeTeamTypeId()).getData();

        //判断用户是暴鸡
        UserIdentityEnum userIdentityEnum = UserIdentityEnum.of(join.getUserIdentity());
        Integer userIdentityCode = typeDetailVO.getBaojiIdentity();

        UserIdentityEnum supportedUserIdentity = UserIdentityEnum.of(userIdentityCode);

        log.info("该游戏类型支持的创建身份类型 -> {}", supportedUserIdentity);
        //非暴鸡暴娘均可
        if (!supportedUserIdentity.equals(UserIdentityEnum.BJ_BN)) {
            //身份不对
            if (UserIdentityEnum.BOSS.equals(userIdentityEnum) || !userIdentityEnum
                    .equals(supportedUserIdentity)) {
                throw new BusinessException(BizExceptionEnum.TEAM_CREATOR_NOT_MATCH);
            }
        }
    }
}

package com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.validator;//package com.kaihei.esportingplus.gamingteam.data.manager.pvp.validator.general;


import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.gamingteam.api.enums.PVPTeamMemberStatusEnum;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.StartTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.after.AfterValidator;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGamePVPFree;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 开车场景、判断是否全员准备
 */
@Slf4j
@Component
@OnScene(includes = {StartTeamScene.class})
public class AllMemberIsPreparedValidator extends AfterValidator<TeamGamePVPFree> {

    /**
     * 执行校验
     */
    @Override
    protected void doValidate() {
        List<TeamMember> teamMemberList = pvpContextHolder.getContext().getTeamMemberList();

        //除队长外全员准备
        if (!teamMemberList.stream()
                .filter(
                        it -> !UserIdentityEnum.LEADER
                                .equals(UserIdentityEnum.of(it.getUserIdentity().intValue()))
                )
                .allMatch(it -> PVPTeamMemberStatusEnum.PREPARE_READY
                        .equals(PVPTeamMemberStatusEnum.of(it.getStatus().intValue())))) {
            throw new BusinessException(BizExceptionEnum.TEAM_MEMBER_NOT_ALL_ONREADY);
        }
    }
}

package com.kaihei.esportingplus.gamingteam.data.manager.core.validator.after;//package com.kaihei.esportingplus.gamingteam.data.manager.pvp.validator.general;

import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.CollectionUtils;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamSequenceVO;
import com.kaihei.esportingplus.gamingteam.data.manager.cache.PVPCompositeCacheManager;
import com.kaihei.esportingplus.gamingteam.data.manager.cache.PVPTeamCacheManager;
import com.kaihei.esportingplus.gamingteam.data.manager.cache.PVPTeamMemberCacheManager;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.BaseTeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.CreateTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.JoinTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.RecreateTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.after.AfterValidator;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 队长组建车队、或老板加入车队时，判断用户是否已经处于其他车队内
 */
@Component
@OnScene(includes = {JoinTeamScene.class, CreateTeamScene.class, RecreateTeamScene.class})
public class UserInOtherTeamValidator extends AfterValidator<TeamGame> {

    @Autowired
    private PVPTeamMemberCacheManager pvpTeamMemberCacheManager;
    @Autowired
    private PVPTeamCacheManager pvpTeamCacheManager;
    @Autowired
    private PVPCompositeCacheManager pvpCompositeCacheManager;

    /**
     * 执行校验
     */
    @Override
    protected void doValidate() {
        PVPContext<TeamGame, TeamMember> context = pvpContextHolder.getContext();
        Integer gameId = context.getTeamGame().getGameId();
        Team team = context.getTeam();

        TeamMember joinTeamMember = context.getJoin();
        if (joinTeamMember == null) {
            return;
        }
        String teamSequenceUserIn = pvpTeamMemberCacheManager
                .findUserTeamSequence(joinTeamMember.getUid(), gameId);
        //首次提交直接返回
        if (teamSequenceUserIn == null) {
            return;
        }

        //在其他车队
        //重复提交直接返回
        if (teamSequenceUserIn.equals(team.getSequence())) {
            context.setJoin(null);
            return;
        }

        //判断加入的车队是否还存在
        Boolean teamUserInExists = pvpTeamCacheManager.teamExists(teamSequenceUserIn);
        if (teamUserInExists) {
            //判断车队是正常的车队:如队长存在
            List<BaseTeamMember> teamMemberPVPList = pvpTeamMemberCacheManager
                    .getTeamMemberList(teamSequenceUserIn,
                            BaseTeamMember.class);

            BaseTeamMember leader = CollectionUtils
                    .find(teamMemberPVPList, it -> UserIdentityEnum.LEADER
                            .equals(UserIdentityEnum.of(it.getUserIdentity().intValue())))
                    .orElse(null);
            System.out.println("team = " + team);
            //队长存在，说明队员已经在一个正常的车队
            if (leader != null) {
                Team teamUserIn = pvpTeamCacheManager.getTeam(teamSequenceUserIn);
                throw new BusinessException(0, BizExceptionEnum.USER_IN_OTHER_TEAM.getErrMsg())
                        .withData(TeamSequenceVO.builder().sequence(teamSequenceUserIn)
                                .group(teamUserIn.getGroupId())
                                .build()
                        );
            }
        }

        //异常流
        pvpCompositeCacheManager.removeCache(team.getSequence());
    }
}

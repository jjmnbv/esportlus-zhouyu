package com.kaihei.esportingplus.gamingteam.data.manager.core.event.rc;//package com.kaihei.esportingplus.gamingteam.data.manager.pvp.event.rc;

import com.kaihei.esportingplus.gamingteam.api.params.ImGroupCommonParams;
import com.kaihei.esportingplus.gamingteam.data.manager.ImMsgService;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.CreateTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.EventPublisher;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 游戏组建成功时、推送队伍创建成功消息
 */
@Slf4j
@Component
@OnScene(includes = CreateTeamScene.class)
public class TeamMemberGroupCreatePublisher extends EventPublisher<TeamGame> {


    @Autowired
    private ImMsgService imMsgService;


    @Override
    protected void doPublish() {
        PVPContext<TeamGame, TeamMember> context = pvpContextHolder.getContext();
        Team contextTeam = context.getTeam();

        TeamMember joinTeamMembers = pvpContextHolder.getContext().getJoin();

        ImGroupCommonParams groupCommonParams = new ImGroupCommonParams();
        groupCommonParams.setGroupId(contextTeam.getGroupId());
        groupCommonParams.setGroupName(contextTeam.getTitle());

        groupCommonParams.setUid(joinTeamMembers.getUid());
        log.info("发送容云消息 -> {}", groupCommonParams);
        imMsgService.createGroup(groupCommonParams);
    }
}

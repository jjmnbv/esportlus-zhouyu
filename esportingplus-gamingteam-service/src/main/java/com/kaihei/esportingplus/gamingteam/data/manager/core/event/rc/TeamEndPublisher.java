package com.kaihei.esportingplus.gamingteam.data.manager.core.event.rc;//package com.kaihei.esportingplus.gamingteam.data.manager.pvp.event.rc;

import com.kaihei.esportingplus.common.constant.ImContent;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.gamingteam.api.params.ImEndTeamMsgParams;
import com.kaihei.esportingplus.gamingteam.data.manager.ImMsgService;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.EndTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.EventPublisher;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 游戏结束时、推送容云结束游戏消息
 */
@Component
@OnScene(includes = EndTeamScene.class)
public class TeamEndPublisher extends EventPublisher<TeamGame> {

    @Autowired
    private ImMsgService imMsgService;


    @Override
    protected void doPublish() {
        PVPContext<TeamGame, TeamMember> context = pvpContextHolder.getContext();
        UserSessionContext user = pvpContextHolder.getUser();
        Team team = context.getTeam();

        List<TeamMember> teamMemberList = context.getTeamMemberList();
        List<String> members = teamMemberList.stream().map(TeamMember::getUid)
                .collect(Collectors.toList());

        ImEndTeamMsgParams imEndTeamMsgParams = new ImEndTeamMsgParams();
        imEndTeamMsgParams.setUid(user.getUid());
        imEndTeamMsgParams.setMembers(members);
        imEndTeamMsgParams.setMsgContent(ImContent.END_TEAM_SERVER_CONTENT);

        imEndTeamMsgParams.setTeamSequence(team.getSequence());
        imEndTeamMsgParams.setGroupId(team.getGroupId());

        imMsgService.endTeam(imEndTeamMsgParams);
    }
}

package com.kaihei.esportingplus.gamingteam.data.manager.core.event.rc;//package com.kaihei.esportingplus.gamingteam.data.manager.pvp.event.rc;

import com.kaihei.esportingplus.common.constant.ImContent;
import com.kaihei.esportingplus.gamingteam.api.enums.ImMemberLeaveType;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupLeavelParams;
import com.kaihei.esportingplus.gamingteam.data.manager.ImMsgService;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.KickOutTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.EventPublisher;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 老板被踢时，推送被踢提示
 */
@Component
@OnScene(includes = KickOutTeamScene.class)
public class TeamMemberKickoutPublisher extends EventPublisher<TeamGame> {

    @Autowired
    private ImMsgService imMsgService;

    @Override
    protected void doPublish() {
        PVPContext<TeamGame, TeamMember> context = pvpContextHolder.getContext();
        List<TeamMember> teamMemberList = context.getTeamMemberList();
        Team contextTeam = context.getTeam();
        List<String> memberIds = teamMemberList.stream().map(TeamMember::getUid)
                .collect(Collectors.toList());

        TeamMember kickoutMember = pvpContextHolder.getContext().getLeave().get(0);

        ImGroupLeavelParams imGroupLeavelParams = new ImGroupLeavelParams();
        imGroupLeavelParams.setMembers(memberIds);
        imGroupLeavelParams.setLeaveType(ImMemberLeaveType.UNACTIVE.getCode());
        imGroupLeavelParams.setToSelfMsgContent(ImContent.LEADER_OUT_MEMBER_CONTENT);
        imGroupLeavelParams.setTeamSequence(contextTeam.getSequence());
        imGroupLeavelParams.setUid(kickoutMember.getUid());
        imGroupLeavelParams.setGroupId(contextTeam.getGroupId());
        imGroupLeavelParams
                .setToOtheMsgContent(String.format("%s被踢出了队伍", kickoutMember.getUsername()));

        imMsgService.leaveGroup(imGroupLeavelParams);
    }
}

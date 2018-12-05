package com.kaihei.esportingplus.gamingteam.data.manager.core.event.rc;//package com.kaihei.esportingplus.gamingteam.data.manager.pvp.event.rc;

import com.kaihei.esportingplus.common.constant.ImContent;
import com.kaihei.esportingplus.gamingteam.api.params.ImDismissGroupParams;
import com.kaihei.esportingplus.gamingteam.data.manager.ImMsgService;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.DismissTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.EventPublisher;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 游戏被解散时、推送容云解散消息
 */
@Component
@OnScene(includes = {DismissTeamScene.class})
public class TeamDismissPublisher extends EventPublisher<TeamGame> {


    @Autowired
    private ImMsgService imMsgService;

    @Override
    protected void doPublish() {
        PVPContext<TeamGame, TeamMember> context = pvpContextHolder.getContext();
        Team contextTeam = context.getTeam();
        String groupId = contextTeam.getGroupId();
        String sequence = contextTeam.getSequence();

        ImDismissGroupParams imDismissGroupParams = new ImDismissGroupParams();
        List<TeamMember> teamMemberList = context.getTeamMemberList();
        List<String> members = teamMemberList.stream().map(TeamMember::getUid)
                .collect(Collectors.toList());

        imDismissGroupParams.setUid(pvpContextHolder.getUser().getUid());
        imDismissGroupParams.setMembers(members);

        imDismissGroupParams.setTeamSequence(sequence);
        imDismissGroupParams.setGroupId(groupId);

        imDismissGroupParams.setMsgContent(ImContent.TEAM_DISMISS_CONTENT);
        log.info("发送容云解散车队消息 ->{}", imDismissGroupParams);
        imMsgService.dismissGroup(imDismissGroupParams);

    }

}

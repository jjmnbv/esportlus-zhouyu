package com.kaihei.esportingplus.gamingteam.data.manager.core.event.rc;//package com.kaihei.esportingplus.gamingteam.data.manager.pvp.event.rc;

import static com.kaihei.esportingplus.common.constant.ImContent.MEMBER_JOIN_TEAM_CONTENT;

import com.alibaba.fastjson.JSON;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupJoinParams;
import com.kaihei.esportingplus.gamingteam.data.manager.ImMsgService;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.JoinTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.EventPublisher;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 老板加入车队时，推送欢迎提醒
 */
@Slf4j
@Component
@OnScene(includes = JoinTeamScene.class)
public class TeamMemberJoinPublisher extends EventPublisher<TeamGame> {


    @Autowired
    private ImMsgService imMsgService;

    @Override
    protected void doPublish() {
        TeamMember joinTeamMember = pvpContextHolder.getContext().getJoin();

        PVPContext<TeamGame, TeamMember> context = pvpContextHolder.getContext();
        Team contextTeam = context.getTeam();

        List<String> members = context.getTeamMemberList().stream().map(TeamMember::getUid)
                .collect(Collectors.toList());

        ImGroupJoinParams imGroupJoinParams = new ImGroupJoinParams();
        imGroupJoinParams.setTeamSequence(contextTeam.getSequence());
        imGroupJoinParams
                .setMsgContent(
                        String.format(MEMBER_JOIN_TEAM_CONTENT, joinTeamMember.getUsername()));
        imGroupJoinParams.setGroupName(contextTeam.getTitle());
        imGroupJoinParams.setGroupId(contextTeam.getGroupId());
        imGroupJoinParams.setUid(joinTeamMember.getUid());
        imGroupJoinParams.setMembers(members);

        log.info("发送加入容云消息 -> {}", JSON.toJSONString(imGroupJoinParams));

        imMsgService.joinGroup(imGroupJoinParams);
    }
}

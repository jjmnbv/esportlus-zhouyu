package com.kaihei.esportingplus.gamingteam.data.manager.core.event.rc;

import com.alibaba.fastjson.JSON;
import com.kaihei.esportingplus.common.constant.ImContent;
import com.kaihei.esportingplus.gamingteam.api.enums.ImMemberLeaveType;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupLeavelParams;
import com.kaihei.esportingplus.gamingteam.data.manager.ImMsgService;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.LeaveTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.EventPublisher;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 老板离开车队时，推送老板离开提示
 */
@Component
@OnScene(includes = {LeaveTeamScene.class})
public class TeamMemberLeavePublisher extends EventPublisher<TeamGame> {

    @Autowired
    private ImMsgService imMsgService;

    @Override
    protected void doPublish() {
        PVPContext<TeamGame, TeamMember> context = pvpContextHolder.getContext();
        Team team = context.getTeam();
        List<TeamMember> teamMemberList = context.getTeamMemberList();
        List<TeamMember> leaves = context.getLeave();
        if (leaves == null || leaves.isEmpty()) {
            return;
        }

        TeamMember leave = leaves.get(0);
        List<String> toMemberIds = teamMemberList.stream().map(TeamMember::getUid)
                .collect(Collectors.toList());

        ImGroupLeavelParams imGroupLeavelParams = new ImGroupLeavelParams();

        imGroupLeavelParams.setGroupId(team.getGroupId());
        imGroupLeavelParams.setUid(leave.getUid());
        imGroupLeavelParams.setTeamSequence(team.getSequence());
        imGroupLeavelParams.setToOtheMsgContent(
                String.format(ImContent.LEAVE_TEAM_CONTENT, leave.getUsername()));
        imGroupLeavelParams.setLeaveType(ImMemberLeaveType.active.getCode());
        imGroupLeavelParams.setMembers(toMemberIds);

        log.info("发送容云离开车队消息 -> {}", JSON.toJSONString(imGroupLeavelParams));

        imMsgService.leaveGroup(imGroupLeavelParams);
    }
}

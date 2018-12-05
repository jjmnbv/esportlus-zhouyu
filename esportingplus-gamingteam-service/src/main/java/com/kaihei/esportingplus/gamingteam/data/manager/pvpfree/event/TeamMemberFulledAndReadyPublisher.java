package com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.event;//package com.kaihei.esportingplus.gamingteam.data.manager.pvp.event.rc;

import com.alibaba.fastjson.JSON;
import com.kaihei.esportingplus.gamingteam.api.params.ImFullMsgParams;
import com.kaihei.esportingplus.gamingteam.data.manager.ImMsgService;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnCondition;
import com.kaihei.esportingplus.gamingteam.data.manager.core.condition.TeamMemberBecomeFulledAndReadyCondition;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.EventPublisher;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGamePVPFree;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 游戏老板满员且全准备时、推送提醒队长开车消息
 */
@Component
@OnCondition(TeamMemberBecomeFulledAndReadyCondition.class)
public class TeamMemberFulledAndReadyPublisher extends EventPublisher<TeamGamePVPFree> {


    @Autowired
    private ImMsgService imMsgService;

    @Override
    protected void doPublish() {
        PVPContext<TeamGame, TeamMember> context = pvpContextHolder.getContext();
        Team team = context.getTeam();
        List<String> members = context.getTeamMemberList().stream().map(TeamMember::getUid)
                .collect(Collectors.toList());
        ImFullMsgParams imFullMsgParams = new ImFullMsgParams();
        imFullMsgParams.setGroupId(team.getSequence());
        imFullMsgParams.setTeamSequence(team.getGroupId());
        imFullMsgParams.setMembers(members);

        imFullMsgParams.setMsgContent("快开车吧、我的大刀已经饥渴难耐了");

        log.info("发送全员准备消息 -> {}", JSON.toJSONString(imFullMsgParams));

        imMsgService.sendFullMembersMsg(imFullMsgParams);
    }
}

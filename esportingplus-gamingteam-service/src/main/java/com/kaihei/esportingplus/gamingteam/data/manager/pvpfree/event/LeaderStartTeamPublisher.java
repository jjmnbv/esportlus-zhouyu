package com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.event;

import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.gamingteam.api.params.ImTeamStartParams;
import com.kaihei.esportingplus.gamingteam.data.manager.ImMsgService;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.StartTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.EventPublisher;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGamePVPFree;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamMemberPVPFree;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@OnScene(includes = {StartTeamScene.class})
public class LeaderStartTeamPublisher extends EventPublisher<TeamGamePVPFree> {

    @Autowired
    private PVPContextHolder<TeamGamePVPFree, TeamMemberPVPFree> pvpContextHolder;

    @Autowired
    private ImMsgService imMsgService;

    /**
     * 由子类实现
     *
     * 不满足条件抛 {@link BusinessException}
     */
    @Override
    protected void doPublish() {
        PVPContext<TeamGamePVPFree, TeamMemberPVPFree> context = pvpContextHolder.getContext();
        Team team = context.getTeam();
        UserSessionContext user = pvpContextHolder.getUser();
        List<String> members = context.getTeamMemberList().stream().map(TeamMemberPVPFree::getUid)
                .collect(Collectors.toList());

        ImTeamStartParams imTeamStartParams = new ImTeamStartParams();
        imTeamStartParams.setGroupId(team.getGroupId());
        imTeamStartParams.setMsgContent("坐稳啦、队长开车了！");
        imTeamStartParams.setTeamSequence(team.getSequence());
        imTeamStartParams.setUid(user.getUid());
        imTeamStartParams.setMembers(members);

        log.info("发送容云开始车队消息 -> {}", imTeamStartParams);

        imMsgService.sendTeamStart(imTeamStartParams);

    }
}

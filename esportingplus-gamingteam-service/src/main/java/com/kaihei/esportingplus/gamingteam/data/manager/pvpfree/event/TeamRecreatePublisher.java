package com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.event;

import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.gamingteam.api.enums.ImTeamMemberStatusEnum;
import com.kaihei.esportingplus.gamingteam.api.params.ImTeamStatusChangeParams;
import com.kaihei.esportingplus.gamingteam.data.manager.ImMsgService;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.RecreateTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.EventPublisher;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGamePVPFree;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@OnScene(includes = RecreateTeamScene.class)
public class TeamRecreatePublisher extends EventPublisher<TeamGamePVPFree> {

    @Autowired
    private ImMsgService imMsgService;

    /**
     * 由子类实现
     *
     * 不满足条件抛 {@link BusinessException}
     */
    @Override
    protected void doPublish() {
        PVPContext<TeamGame, TeamMember> context = pvpContextHolder.getContext();
        Team team = pvpContextHolder.getContext().getTeam();

        UserSessionContext user = pvpContextHolder.getUser();

        ImTeamStatusChangeParams params = new ImTeamStatusChangeParams();
        params.setGroupId(team.getGroupId());
        params.setStatusEnum(ImTeamMemberStatusEnum.Another_order);
        params.setTeamSequence(team.getSequence());
        params.setUid(user.getUid());
        params.setMembers(new ArrayList<>(context.getUidTeamMemberMap().keySet()));

        log.info("发送融云重建车队消息 -> {}", params);

        imMsgService.sendTeamStatusChange(params);
    }
}

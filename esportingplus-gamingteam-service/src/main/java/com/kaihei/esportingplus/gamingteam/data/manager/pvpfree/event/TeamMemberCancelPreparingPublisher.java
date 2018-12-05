package com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.event;

import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.gamingteam.api.enums.ImTeamMemberStatusEnum;
import com.kaihei.esportingplus.gamingteam.api.params.ImTeamStatusChangeParams;
import com.kaihei.esportingplus.gamingteam.data.manager.ImMsgService;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.BossCancelPrepareScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.EventPublisher;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGamePVPFree;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamMemberPVPFree;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@OnScene(includes = {BossCancelPrepareScene.class})
public class TeamMemberCancelPreparingPublisher extends EventPublisher<TeamGamePVPFree> {

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
        UserSessionContext user = pvpContextHolder.getUser();
        PVPContext<TeamGamePVPFree, TeamMemberPVPFree> context = pvpContextHolder.getContext();
        Team team = context.getTeam();
        List<TeamMemberPVPFree> teamMemberList = context.getTeamMemberList();
        List<String> members = teamMemberList.stream().map(TeamMemberPVPFree::getUid)
                .collect(Collectors.toList());

        ImTeamStatusChangeParams imTeamStatusChangeParams = new ImTeamStatusChangeParams();
        imTeamStatusChangeParams.setStatusEnum(ImTeamMemberStatusEnum.CANCEL_READY);
        imTeamStatusChangeParams.setTeamSequence(team.getSequence());
        imTeamStatusChangeParams.setMembers(members);

        imTeamStatusChangeParams.setUid(user.getUid());
        imTeamStatusChangeParams.setGroupId(team.getGroupId());

        log.info("发送融云取消准备消息 ->{}", imTeamStatusChangeParams);
        imMsgService.sendTeamStatusChange(imTeamStatusChangeParams);

    }
}

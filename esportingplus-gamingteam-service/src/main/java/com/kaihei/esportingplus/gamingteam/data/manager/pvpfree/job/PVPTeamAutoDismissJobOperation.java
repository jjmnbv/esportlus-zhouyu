package com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.job;

import com.kaihei.esportingplus.common.data.JsonSerializable;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.job.Job;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.CreateTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.RecreateTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.job.PVPTeamAutoDismissJobOperation.AutoDismissParam;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGamePVPFree;
import com.kaihei.esportingplus.gamingteam.domain.service.PVPTeamFreeService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 老板支付超时任务
 */
@Component
@OnScene(includes = {CreateTeamScene.class, RecreateTeamScene.class})
public class PVPTeamAutoDismissJobOperation extends Job<TeamGamePVPFree, AutoDismissParam> {

    @Autowired
    private PVPTeamFreeService pvpTeamFreeService;

    @Value("${team.pvpfree.auto-dismiss-seconds}")
    private Integer pvpFreeTeamAutoDismissSeconds;

    /**
     * 执行作业.
     *
     * @param autoDismissParam 参数
     */
    @Override
    protected void execute(AutoDismissParam autoDismissParam) {
        //造user
        UserSessionContext userSessionContext = new UserSessionContext();
        userSessionContext.setUid(autoDismissParam.uid);
        pvpContextHolder.setUser(userSessionContext);

        pvpTeamFreeService.dismissTeam(autoDismissParam.teamSequence);
    }

    /**
     * 由子类实现
     *
     * 进行一些Service后续的操作
     */
    @Override
    protected void doOperate() {
        PVPContext<TeamGame, TeamMember> context = pvpContextHolder.getContext();
        Team team = context.getTeam();
        UserSessionContext user = pvpContextHolder.getUser();

        AutoDismissParam autoDismissParam = AutoDismissParam.builder()
                .teamSequence(team.getSequence())
                .uid(user.getUid()).build();

        addJob(team.getSequence(), pvpFreeTeamAutoDismissSeconds * 1000,
                autoDismissParam.toJSONString());
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class AutoDismissParam implements JsonSerializable {

        private String uid;

        private String teamSequence;
    }
}

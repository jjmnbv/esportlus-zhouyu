package com.kaihei.esportingplus.gamingteam.data.manager.core.context;

import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext.PVPContextBuilder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.Scene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author 谢思勇
 *
 * 缓存及、备份持有
 */
@Slf4j
@Component
public class PVPContextHolder<TG extends TeamGame, TM extends TeamMember> {

    /**
     * 车队信息
     */
    private final ThreadLocal<PVPContext<TG, TM>> context = new InheritableThreadLocal<>();
    /**
     * 车队信息备份
     */
    private final ThreadLocal<PVPContext<TG, TM>> backup = new InheritableThreadLocal<>();

    /**
     * 用户
     */
    private final ThreadLocal<UserSessionContext> user = new InheritableThreadLocal<>();

    /**
     * 场景存储位置
     */
    private final ThreadLocal<Scene> scene = new InheritableThreadLocal<>();

    public UserSessionContext getUser() {
        return user.get();
    }

    public void setUser(UserSessionContext user) {
        this.user.set(user);
    }

    public PVPContext<TG, TM> getContext() {
        return context.get();
    }

    public void setContext(PVPContext<TG, TM> context) {
        this.context.set(context);
        this.createBackup();
    }

    public PVPContext<TG, TM> getBackup() {
        return backup.get();
    }

    public void setBackup(PVPContext<TG, TM> backup) {
        this.backup.set(backup);
    }

    public Scene getScene() {
        return scene.get();
    }

    public void setScene(Scene scene) {
        log.info("当前为 {} 场景", scene.getName());
        this.scene.set(scene);
    }

    private void createBackup() {
        PVPContext<TG, TM> pvpContext = context.get();
        PVPContextBuilder builder = PVPContext.<TG, TM>builder();
        Team team = pvpContext.getTeam();
        //Team
        if (team != null) {
            builder.team(team.cast(team.getClass()));
        }

        //teamGame
        TG teamGame = pvpContext.getTeamGame();
        if (teamGame != null) {
            builder.teamGame(teamGame.cast(teamGame.getClass()));
        }

        //TM
        List<TM> teamMemberList = pvpContext.getTeamMemberList();
        if (teamMemberList != null) {
            List<? extends TeamMember> tml = teamMemberList.stream()
                    .map(tm -> tm.cast(tm.getClass()))
                    .collect(Collectors.toList());
            builder.teamMemberList(tml);
        }
        this.setBackup(builder.build());
    }

    public void clean() {
        scene.remove();
        user.remove();
        context.remove();
        backup.remove();
    }
}

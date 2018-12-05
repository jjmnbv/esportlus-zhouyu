package com.kaihei.esportingplus.gamingteam.data.manager.core.populator;

import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.JoinTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.UnnecessaryPopulator;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * @author 谢思勇
 *
 * 用户段位名填充器
 */
@Component
@OnScene(includes = {JoinTeamScene.class})
public class TeamMemberDanNamePopulator extends UnnecessaryPopulator<TeamGame> {

    /**
     * 由子类实现
     *
     * 满足条件就做参数的填充操作
     */
    @Override
    protected void doPopulate() {
        TeamMember join = pvpContextHolder.getContext().getJoin();
        Optional.of(join).map(TeamMember::getGameDanId)
                .map(this::converte).ifPresent(join::setGameDanName);
    }
}

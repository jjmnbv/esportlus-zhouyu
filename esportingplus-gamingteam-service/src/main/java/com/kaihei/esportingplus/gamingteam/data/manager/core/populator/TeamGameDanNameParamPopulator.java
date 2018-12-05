package com.kaihei.esportingplus.gamingteam.data.manager.core.populator;

import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.CreateTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.UnnecessaryPopulator;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * @author 谢思勇
 *
 * 创建车队
 */
@Component
@OnScene(includes = {CreateTeamScene.class})
public class TeamGameDanNameParamPopulator extends UnnecessaryPopulator<TeamGame> {

    /**
     * 由子类实现
     *
     * 满足条件就做参数的填充操作
     */
    @Override
    protected void doPopulate() {
        PVPContext<TeamGame, TeamMember> context = pvpContextHolder.getContext();
        TeamGame teamGame = context.getTeamGame();
        //最低段位名
        Optional.ofNullable(teamGame.getLowerDanId())
                .map(this::converte).ifPresent(teamGame::setLowerDanName);

        //最高段位名
        Optional.ofNullable(teamGame.getUpperDanId())
                .map(this::converte).ifPresent(teamGame::setUpperDanName);
    }
}

package com.kaihei.esportingplus.gamingteam.data.manager.core.condition;//package com.kaihei.esportingplus.gamingteam.data.manager.pvp.condition;

import com.kaihei.esportingplus.common.enums.GameResultEnum;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.EndTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.Condition;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGameResult;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 判断当前为队长结束车队、且存在未玩结局的场景
 */
@Component
@OnScene(includes = EndTeamScene.class)
public class TeamEndWithNotPlayCondition extends Condition<TeamGame> {

    /**
     * 由子类实现
     *
     * 判断是否满足条件
     */
    @Override
    protected boolean onCondition() {
        PVPContext<TeamGame, TeamMember> context = pvpContextHolder.getContext();
        List<TeamGameResult> teamGameResults = context.getTeamGameResults();
        //非解散
        if (teamGameResults != null) {
            for (int i = 0; i < teamGameResults.size(); i++) {
                TeamGameResult teamGameResult = teamGameResults.get(i);
                GameResultEnum gameResult = GameResultEnum.fromCode(teamGameResult.getGameResult());
                //非未打
                if (gameResult.equals(GameResultEnum.HOURS_NOT_PLAY) || gameResult
                        .equals(GameResultEnum.ROUNDS_NOT_PLAY)) {
                    log.info("---->|达成 游戏以未玩结束游戏 条件");
                    return true;
                }
            }
        }
        return false;
    }

}

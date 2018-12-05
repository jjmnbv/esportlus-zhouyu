package com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.operation;

import com.kaihei.esportingplus.api.vo.freeteam.GameDanRangeVO;
import com.kaihei.esportingplus.gamingteam.data.manager.TeamServiceSupport;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnCondition;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.condition.BossLeaveBeforeTeamRunningCondition;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.JoinTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.KickOutTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.RecreateTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.after.AfterOperation;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGamePVPFree;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamMemberPVPFree;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 老板进出车队、重新计算 最高最低段位、
 */
@Component
@OnScene(includes = {JoinTeamScene.class, KickOutTeamScene.class, RecreateTeamScene.class})
@OnCondition(BossLeaveBeforeTeamRunningCondition.class)
public class TeamFreeMemberJoinDanReCaculateOperation extends
        AfterOperation<TeamGamePVPFree> implements Ordered {

    @Autowired
    private PVPContextHolder<TeamGamePVPFree, TeamMemberPVPFree> pvpContextHolder;
    @Autowired
    private TeamServiceSupport teamServiceSupport;

    @Override
    public void doOperate() {
        PVPContext<TeamGamePVPFree, TeamMemberPVPFree> context = pvpContextHolder.getContext();
        TeamGamePVPFree teamGame = context.getTeamGame();
        Integer gameId = teamGame.getGameId();
        List<Integer> danIds = context.getTeamMemberList().stream()
                .map(TeamMemberPVPFree::getGameDanId)
                .collect(Collectors.toList());

        GameDanRangeVO gameDanRangeVO = teamServiceSupport
                .calculateGamingTeamDanRange(gameId, danIds, teamGame.getLowerDanId(),
                        teamGame.getUpperDanId());

        teamGame.setUpperDanId(gameDanRangeVO.getUpperDanId());
        teamGame.setLowerDanId(gameDanRangeVO.getLowerDanId());

        teamGame.setGmtModified(new Date());

    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE - 2;
    }
}

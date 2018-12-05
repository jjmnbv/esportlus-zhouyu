package com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.populator;

import com.kaihei.esportingplus.api.feign.FreeTeamTypeServiceClient;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamTypeDetailVO;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.CreateTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.NecessaryPopulator;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGamePVPFree;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamMemberPVPFree;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@OnScene(includes = {CreateTeamScene.class})
public class TeamGamePVPFreeNecessaryParamPopulator extends NecessaryPopulator<TeamGamePVPFree> {

    @Autowired
    private FreeTeamTypeServiceClient freeTeamTypeServiceClient;
    @Autowired
    private PVPContextHolder<TeamGamePVPFree, TeamMemberPVPFree> pvpContextHolder;

    /**
     * 由子类实现
     *
     * 满足条件就做参数的填充操作
     */
    @Override
    protected void doPopulate() {
        PVPContext<TeamGamePVPFree, TeamMemberPVPFree> context = pvpContextHolder.getContext();
        Team team = context.getTeam();
        TeamGamePVPFree teamGame = context.getTeamGame();
        //车队类型
        FreeTeamTypeDetailVO typeDetailVO = freeTeamTypeServiceClient
                .getFreeTeamTypeById(teamGame.getFreeTeamTypeId()).getData();
        Integer maxPositionCount = typeDetailVO.getMaxPositionCount();

        team.setOriginalPositionCount(maxPositionCount);
        team.setActuallyPositionCount(maxPositionCount);

        //设置GameID
        teamGame.setGameId(typeDetailVO.getGame().getDictId());

        teamGame.setFreeTeamTypeName(typeDetailVO.getName());

        teamGame.setGmtModified(new Date());
    }

}

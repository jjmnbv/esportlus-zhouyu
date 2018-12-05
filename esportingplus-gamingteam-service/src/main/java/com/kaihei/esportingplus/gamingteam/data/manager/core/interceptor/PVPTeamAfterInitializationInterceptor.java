package com.kaihei.esportingplus.gamingteam.data.manager.core.interceptor;

import static com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPConstant.PVP_TEAM_AFTER_INIT_AOP_ORDER;

import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.TeamOperation;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.repository.CompositeRepository;
import com.kaihei.esportingplus.gamingteam.data.repository.TeamRepository;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @author 谢思勇
 *
 * 根据注解@see {@link PVPTeamAfterInitializationInterceptor}
 *
 * 在有该注解的方法前初始化 {@link PVPContext}并放入 {@link PVPContextHolder}
 *
 * AOP序号为102
 */
@Slf4j
@Aspect
@Component
public class PVPTeamAfterInitializationInterceptor implements Ordered {

    @Autowired
    private PVPContextHolder<TeamGame, TeamMember> pvpContextHolder;

    @Value("${spring.datasource.druid.connectionInitSqls}")
    private String emojiSql;

    @Autowired
    private CompositeRepository compositeRepository;

    @Autowired
    private TeamRepository teamRepository;

    @AfterReturning("@annotation(teamOperation)")
    public void afterReturnning(TeamOperation teamOperation) {
        if (!teamOperation.init()) {
            return;
        }
        long start = System.currentTimeMillis();

        log.info("PVPTeamInitializationInterceptor 后处理 -> 初始化数据存盘");
        PVPContext<TeamGame, TeamMember> pvpContext = pvpContextHolder.getContext();
        Team team = pvpContext.getTeam();
        TeamGame teamGame = pvpContext.getTeamGame();
        teamRepository.setNames();

        //存Team
        compositeRepository.insert(team);
        team.setGmtModified(new Date());

        log.info("Team存库 {}", team);

        //存TeamGamePVP
        teamGame.setTeamId(team.getId());
        compositeRepository.insertSelective(teamGame);
        teamGame.setGmtModified(new Date());

        TeamMember join = pvpContext.getJoin();
        if (join != null) {
            join.setTeamId(team.getId());
        }

        List<TeamMember> restores = pvpContext.getRestores();
        if (restores != null && !restores.isEmpty()) {
            restores.stream().filter(it -> it.getTeamId() == null)
                    .forEach(it -> it.setTeamId(team.getId()));
        }

        log.info("TeamGame存库 {}", teamGame);

        log.info("数据存入Mysql 使用时间 -> {}", System.currentTimeMillis() - start);
    }

    @Override
    public int getOrder() {
        return PVP_TEAM_AFTER_INIT_AOP_ORDER;
    }
}

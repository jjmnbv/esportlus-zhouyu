package com.kaihei.esportingplus.gamingteam.data.manager.core.interceptor;

import static com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPConstant.PVP_TEAM_AFTER_OPERATION_AOP_ORDER;

import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.gamingteam.data.manager.cache.PVPCompositeCacheManager;
import com.kaihei.esportingplus.gamingteam.data.manager.cache.PVPTeamCacheManager;
import com.kaihei.esportingplus.gamingteam.data.manager.cache.PVPTeamGameCacheManager;
import com.kaihei.esportingplus.gamingteam.data.manager.cache.PVPTeamMemberCacheManager;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.TeamOperation;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.repository.CompositeRepository;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGameResult;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
/**
 * 类设计意义：{@link com.kaihei.esportingplus.gamingteam.domain.service.PVPTeamServiceImpl}只需关注业务逻辑，数据操作完更新
 * {@link PVPContextHolder}内的对象即可
 *
 * @author 谢思勇
 *
 * 根据注解 @see {@link TeamOperation} 实现从Redis加载 {@link PVPContext}到 {@link PVPContextHolder}
 * 并在完成操作后根据数据是否被修改决定是否更新缓存
 *
 * {@link # before(JoinPoint, TeamOperation)} 从Redis读取缓存
 *
 * {@link # afterReturn()} 更新缓存
 */
@Slf4j
@Aspect
@Component
public class PVPTeamAfterOperationInterceptor implements Ordered {

    @Autowired
    private PVPTeamMemberCacheManager pvpTeamMemberCacheManager;
    @Autowired
    private PVPTeamCacheManager pvpTeamCacheManager;
    @Autowired
    private PVPTeamGameCacheManager pvpTeamGameCacheManager;
    @Autowired
    private PVPContextHolder<TeamGame, TeamMember> pvpContextHolder;
    @Autowired
    private PVPCompositeCacheManager pvpCompositeCacheManager;
    @Autowired
    private CompositeRepository compositeRepository;

    @Override
    public int getOrder() {
        return PVP_TEAM_AFTER_OPERATION_AOP_ORDER;
    }

    @AfterReturning("@annotation(teamOperation)")
    public void afterReturn(JoinPoint joinPoint, TeamOperation teamOperation) {
        long start = System.currentTimeMillis();

        if (!teamOperation.end()) {
            log.info("PVPTeamOperationInterceptor后处理 -> 缓存处理载器");
            storeTeamMemberPVPList(teamOperation);
            storeTeam();
            storeTeamGamePVP();

            log.info("重新缓存Redis Team数据 ，使用时间 ->{}", System.currentTimeMillis() - start);
        } else {
            saveAll();
            log.info("Team数据存库 ，使用时间 ->{}", System.currentTimeMillis() - start);
            pvpCompositeCacheManager.removeCache(getTeamSequence(joinPoint, teamOperation));
        }
    }
    
    private void saveAll() {
        PVPContext<TeamGame, TeamMember> pvpContext = pvpContextHolder.getContext();
        Team contextTeam = pvpContext.getTeam();
        TeamGame teamGame = pvpContext.getTeamGame();
        compositeRepository.updateByPrimaryKeySelective(contextTeam);
        compositeRepository.updateByPrimaryKeySelective(teamGame);
        pvpContext.getTeamMemberList().forEach(compositeRepository::insertSelective);

        List<TeamGameResult> teamGameResults = pvpContextHolder.getContext().getTeamGameResults();
        if (teamGameResults != null && !teamGameResults.isEmpty()) {
            teamGameResults.forEach(compositeRepository::insertSelective);
        }
    }


    /**
     * 缓存TeamMemberPVPList
     */
    private void storeTeamMemberPVPList(TeamOperation teamOperation) {
        PVPContext<TeamGame, TeamMember> pvpContext = pvpContextHolder.getContext();
        Team team = pvpContext.getTeam();

        TeamGame teamGame = pvpContext.getTeamGame();

        String teamSequence = team.getSequence();

        TeamMember join = pvpContext.getJoin();

        //新入队的人
        if (join != null) {
            log.info("上车用户->{}", join);
            pvpTeamMemberCacheManager.storeTeamMember(join, teamGame.getGameId(), teamSequence);
        }

        //需要重新缓存的人
        List<TeamMember> restores = pvpContext.getRestores();
        if (restores != null && !restores.isEmpty()) {
            log.info("更新用户->{}", restores);
            restores.forEach(it -> pvpTeamMemberCacheManager
                    .restoreTeamMember(it, teamGame.getGameId(), teamSequence));
        }

        //需要从队里去掉的人
        List<TeamMember> leaves = pvpContext.getLeave();
        if (leaves == null || leaves.isEmpty()) {
            return;
        }
        leaves.forEach(leave -> {
            log.info("下车用户->{}", leave);
            pvpTeamMemberCacheManager
                    .removeRedisTeamMember(teamGame.getGameId(), teamSequence, leave.getUid());
        });

    }

    /**
     * 与备份对比最终修改时间 有改动执行存Redis操作
     *
     * 缓存Team
     */
    private void storeTeam() {
        Team team = pvpContextHolder.getContext().getTeam();
        Team backUpTeam = pvpContextHolder.getBackup().getTeam();

        //判断数据被修改
        if (!backUpTeam.getGmtModified().equals(team.getGmtModified())) {
            pvpTeamCacheManager.storeTeam(team);
        }
    }

    /**
     * 与备份对比最终修改时间 有改动执行存Redis操作
     *
     * 缓存TeamMemberPVP
     */
    private void storeTeamGamePVP() {
        PVPContext pvpContext = pvpContextHolder.getContext();
        TeamGame teamGame = pvpContextHolder.getContext().getTeamGame();

        TeamGame backUpTeamGame = pvpContextHolder.getBackup().getTeamGame();

        //判断数据被修改
        if (backUpTeamGame == null || !backUpTeamGame.getGmtModified()
                .equals(teamGame.getGmtModified())) {
            Team team = pvpContext.getTeam();
            pvpTeamGameCacheManager.storeTeamGame(team.getSequence(), teamGame);
        }
    }

    /**
     * 获取teamSequence
     *
     * @return 从参数中获取 teamSequence
     */
    private String getTeamSequence(JoinPoint joinPoint, TeamOperation teamOperation) {
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();

        HashMap<String, Object> params = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            params.put(parameterNames[i], args[i]);
        }
        return String.valueOf(ObjectTools.getParamByPath(teamOperation.sequencePath(), params));
    }
}

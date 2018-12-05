package com.kaihei.esportingplus.gamingteam.data.manager.core.interceptor;

import static com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPConstant.PVP_TEAM_BEFORE_OPERATION_AOP_ORDER;

import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.gamingteam.api.params.pvp.PVPTeamEndParams;
import com.kaihei.esportingplus.gamingteam.data.manager.cache.PVPTeamCacheManager;
import com.kaihei.esportingplus.gamingteam.data.manager.cache.PVPTeamGameCacheManager;
import com.kaihei.esportingplus.gamingteam.data.manager.cache.PVPTeamMemberCacheManager;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.TeamOperation;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.enums.TeamGameEnum;
import com.kaihei.esportingplus.gamingteam.data.manager.core.enums.TeamMemberEnum;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.EndTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.Scene;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGameResult;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
 * {@link #before(JoinPoint, TeamOperation)} 从Redis读取缓存
 *
 * {@link # afterReturn()} 更新缓存
 */
@Slf4j
@Aspect
@Component
public class PVPTeamBeforeOperationInterceptor implements Ordered, InitializingBean {

    @Autowired
    private PVPTeamMemberCacheManager pvpTeamMemberCacheManager;
    @Autowired
    private PVPTeamCacheManager pvpTeamCacheManager;
    @Autowired
    private PVPTeamGameCacheManager pvpTeamGameCacheManager;
    @Autowired
    private PVPContextHolder<TeamGame, TeamMember> pvpContextHolder;
    protected Map<Class<? extends Scene>, Scene> scenesMap;
    @Autowired
    private ApplicationContext applicationContext;
    @Override
    public int getOrder() {
        return PVP_TEAM_BEFORE_OPERATION_AOP_ORDER;
    }

    @SuppressWarnings("unchecked")
    @Before("@annotation(teamOperation)")
    public void before(JoinPoint joinPoint, TeamOperation teamOperation) {
        pvpContextHolder.setScene(scenesMap.get(teamOperation.scene()));
        if (teamOperation.init()) {
            return;
        }
        log.info("PVPTeamOperationInterceptor前处理 -> 缓存加载器");
        if (pvpContextHolder.getUser() == null) {
            pvpContextHolder.setUser(UserSessionContext.getUser());
        }
        long start = System.currentTimeMillis();

        //从Redis读取缓存
        String teamSequence = getTeamSequence(joinPoint, teamOperation);

        Team team = pvpTeamCacheManager.getTeam(teamSequence);

        TeamGame teamGame = pvpTeamGameCacheManager.getTeamGame(teamSequence,
                TeamGameEnum.of(team.getTeamType().intValue()));

        List<TeamMember> teamMemberArrayList = (List<TeamMember>) pvpTeamMemberCacheManager
                .getTeamMemberList(teamSequence, TeamMemberEnum.of(team.getTeamType().intValue()));
        log.info("读取Redis Team 数据使用时间 -> {}", System.currentTimeMillis() - start);
        //把需要的参数放入holder
        PVPContext pvpContext = PVPContext.builder().team(team)
                .teamGame(teamGame)
                .teamMemberList(teamMemberArrayList)
                .build();

        pvpContextHolder.setContext(pvpContext);
        endTeamParamAnalizy(joinPoint, teamOperation);
    }

    public void endTeamParamAnalizy(JoinPoint joinPoint, TeamOperation teamOperation) {
        //不是结束车队场景直接返回
        if (!(teamOperation.scene().equals(EndTeamScene.class))) {
            return;
        }

        PVPContext<TeamGame, TeamMember> context = pvpContextHolder.getContext();
        Team contextTeam = context.getTeam();
        Date now = new Date();

        PVPTeamEndParams pvpTeamEndParams = null;
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof PVPTeamEndParams) {
                pvpTeamEndParams = (PVPTeamEndParams) arg;
                break;
            }
        }

        if (pvpTeamEndParams == null) {
            return;
        }

        List<TeamGameResult> teamGameResults = new ArrayList<>();
        context.setTeamGameResults(teamGameResults);

        for (int i = 0; i < pvpTeamEndParams.getGameResult().size(); i++) {
            TeamGameResult teamGameResult = TeamGameResult.builder()
                    .resultSequence(i)
                    .gmtModified(now)
                    .gmtCreate(now)
                    .gameResult(pvpTeamEndParams.getGameResult().get(i))
                    .teamId(contextTeam.getId())
                    .build();
            teamGameResults.add(teamGameResult);
            if (i == 0) {
                if (pvpTeamEndParams.getScreenshots() != null && !pvpTeamEndParams
                        .getScreenshots()
                        .isEmpty()) {
                    StringJoiner stringJoiner = new StringJoiner(",");
                    pvpTeamEndParams.getScreenshots().forEach(stringJoiner::add);
                    String screenshots = stringJoiner.toString();
                    teamGameResult.setScreenshot(screenshots);
                }
            }
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

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Scene> sceneMap = applicationContext.getBeansOfType(Scene.class);
        scenesMap = sceneMap.values().stream().collect(Collectors.toMap(Scene::getClass, e -> e));
    }
}

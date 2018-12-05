package com.kaihei.esportingplus.gamingteam.data.manager.core.interceptor;

import static com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPConstant.PVP_TEAM_CLEAN_AOP_ORDER;

import com.kaihei.esportingplus.gamingteam.data.manager.core.Performer;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @author 谢思勇
 *
 * 事后
 */
@Slf4j
@Aspect
@Component
public class PVPTeamCleanInterceptor implements Ordered {

    @Autowired
    private PVPContextHolder<TeamGame, TeamMember> pvpContextHolder;
    @Autowired(required = false)
    private List<Performer> performers;

    @Override
    public int getOrder() {
        return PVP_TEAM_CLEAN_AOP_ORDER;
    }

    @AfterReturning("@annotation(com.kaihei.esportingplus.gamingteam.data.manager.core.anno.TeamOperation)")
    public void afterReturn() {
        pvpContextHolder.clean();
        cleanPerforms();
    }

    @AfterThrowing(value = "@annotation(com.kaihei.esportingplus.gamingteam.data.manager.core.anno.TeamOperation)")
    public void afterThrowing() {
        pvpContextHolder.clean();
        cleanPerforms();
    }

    private void cleanPerforms() {
        Optional.ofNullable(performers)
                .filter(it -> !it.isEmpty())
                .ifPresent(it -> it.forEach(Performer::afterPerform));
    }
}

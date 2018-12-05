package com.kaihei.esportingplus.gamingteam.data.manager.core.interceptor;

import static com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPConstant.BEFORE_RESTORE_ANALIZE_AOP_ORDER;

import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.TeamOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class PVPTeamBeforeRestoreAnalizeInterceptor implements Ordered {

    @Autowired
    private PVPTeamAnalizeInterceptor pvpTeamAnalizeInterceptor;

    /**
     * 比较context和备份内容，筛选出被修改过的TeamMember
     */
    @SuppressWarnings("unchecked")
    @AfterReturning("@annotation(teamOperation)")
    public void afterReturning(TeamOperation teamOperation) {
        log.info("数据缓存前最终处理....");
        pvpTeamAnalizeInterceptor.afterReturningAnalyze();
    }

    @Override
    public int getOrder() {
        return BEFORE_RESTORE_ANALIZE_AOP_ORDER;
    }
}

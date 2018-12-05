package com.kaihei.esportingplus.gamingteam.data.manager.core.interceptor;

import static com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPConstant.AFTER_RESTORE_PERFORMER_AOP_ORDER;

import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.TeamOperation;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AfterRestorePerformerInterceptor extends PerformerInterceptor implements Ordered {

    @AfterReturning("@annotation(teamOperation)")
    public void afterReturning(TeamOperation teamOperation) {
        operationConsumer.accept(afterRestoreOperations);
    }

    @Override
    public int getOrder() {
        return AFTER_RESTORE_PERFORMER_AOP_ORDER;
    }
}

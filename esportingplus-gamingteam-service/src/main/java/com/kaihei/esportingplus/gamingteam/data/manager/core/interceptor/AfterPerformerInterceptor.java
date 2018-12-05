package com.kaihei.esportingplus.gamingteam.data.manager.core.interceptor;

import static com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPConstant.PERFORMER_AFTER_AOP_ORDER;

import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.TeamOperation;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AfterPerformerInterceptor extends PerformerInterceptor implements Ordered {

    @AfterReturning("@annotation(teamOperation)")
    public void afterReturning(TeamOperation teamOperation) {
        necessaryPopulatorConsumer.accept(necessaryPopulators);
        validatorConsumer.accept(afterValidators);
        operationConsumer.accept(afterOperations);
        unnecessaryPopulatorConsumer.accept(unnecessaryPopulators);
        publisherConsumer.accept(eventPublishers);
    }

    @Override
    public int getOrder() {
        return PERFORMER_AFTER_AOP_ORDER;
    }
}

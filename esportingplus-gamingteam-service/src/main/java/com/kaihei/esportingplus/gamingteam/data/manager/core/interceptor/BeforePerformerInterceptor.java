package com.kaihei.esportingplus.gamingteam.data.manager.core.interceptor;

import static com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPConstant.PERFORMER_BEFORE_AOP_ORDER;

import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.TeamOperation;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BeforePerformerInterceptor extends PerformerInterceptor implements Ordered {

    @Before("@annotation(teamOperation)")
    public void before(TeamOperation teamOperation) {
        //执行
        validatorConsumer.accept(beforeValidators);
        operationConsumer.accept(beforeOperations);
    }

    @Override
    public int getOrder() {
        return PERFORMER_BEFORE_AOP_ORDER;
    }
}

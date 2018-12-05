package com.kaihei.esportingplus.gamingteam.data.manager.core.interceptor;

import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.MockUser;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import java.util.HashMap;
import java.util.Map;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 在有 {@link MockUser}注解的方法上、根据入参Mock一个User
 */
@Aspect
@Component
public class PVPMockUserInterceptor implements Ordered {

    @Autowired
    private PVPContextHolder<TeamGame, TeamMember> pvpContextHolder;

    @Before("@annotation(mockUser)")
    public void before(JoinPoint joinPoint, MockUser mockUser) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            map.put(parameterNames[i], args[i]);
        }

        Object uid = ObjectTools.getParamByPath(mockUser.uid(), map);
        Object username = ObjectTools.getParamByPath(mockUser.username(), map);
        Object avatar = ObjectTools.getParamByPath(mockUser.avatar(), map);
        Object chickenId = ObjectTools.getParamByPath(mockUser.chickenId(), map);
        UserSessionContext userSessionContext = new UserSessionContext();
        userSessionContext.setUid(String.valueOf(uid));
        userSessionContext.setUsername(String.valueOf(username));
        userSessionContext.setAvatar(String.valueOf(avatar));
        userSessionContext.setChickenId(String.valueOf(chickenId));

        pvpContextHolder.setUser(userSessionContext);
    }


    @Override
    public int getOrder() {
        return 0;
    }
}

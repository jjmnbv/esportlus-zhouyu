package com.kaihei.esportingplus.common.lock;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;


/**
 * 防止用户多次点击重复提交请求
 *
 * @author liangyi
 */
@Aspect
@Component
@Slf4j
public class PreventRepeatOperationAspect implements Ordered {

    private CacheManager cacheManager = CacheManagerFactory.create();

    /**
     * 切入点为加了 @PreventResubmit 注解
     */
    @Pointcut("@annotation(com.kaihei.esportingplus.common.lock.PreventRepeatOperation)")
    public void preventRepeatOperationCut() {

    }

    @Before("preventRepeatOperationCut()")
    public void process(JoinPoint joinPoint) {
        Method method = getPointMethod(joinPoint);
        PreventRepeatOperation pre = method.getAnnotation(PreventRepeatOperation.class);
        if (pre != null) {
            String uid = UserSessionContext.getUser().getUid();
            String methodName = method.getName();
            String proKey = String.format(RedisKey.PREVENT_REPEAT_OPERATION, uid, methodName);

            Boolean exists = cacheManager.exists(proKey);
            if (!exists) {
                // 标记不存在, 放置标记并放行
                cacheManager.set(proKey, uid, pre.expireTime());
                return;
            }
            log.warn(">> 用户[{}]重复请求了[{}]!", uid, methodName);
            throw new BusinessException(BizExceptionEnum.TEAM_OPERATE_TOO_FAST);
        }
    }

    /**
     * 获取切入点的方法
     */
    private Method getPointMethod(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return methodSignature.getMethod();
    }

    @Override
    public int getOrder() {
        return 1;
    }

}
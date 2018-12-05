package com.kaihei.esportingplus.gamingteam.aspect;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.gamingteam.annotation.RedisDistlock;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * aop 切面处理, 实现分布式加锁操作
 * 这里通过反射获取方法上包含有 sequence 的参数(或类里面含有 sequence 字段)
 * 这就要求前端传递的参数名一定要是 sequence,个人感觉这种方式还是侵入性太高, 待后期优化
 * @author liangyi
 */
@Aspect
@Component
public class RedisDistlockAspect {

    private static final Logger logger = LoggerFactory.getLogger(RedisDistlockAspect.class);

    private static CacheManager cacheManager = CacheManagerFactory.create();

    /**
     * 切入点为加了 @RedissonLock 注解
     */
    @Pointcut("@annotation(com.kaihei.esportingplus.gamingteam.annotation.RedisDistlock)")
    public void RedissonLockAspect(){

    }

    @Around("RedissonLockAspect()")
    public Object lockAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Method method = null;
        RedisDistlock redisDistlock;
        RLock rLock = null;
        long start = 0L;
        try {
            method = getPointMethod(proceedingJoinPoint);
            // 获取方法上的分布式锁注解
            redisDistlock = method.getAnnotation(RedisDistlock.class);
            long expireTime = redisDistlock.expireTime();
            RedissonClient redissonClient = cacheManager.redissonClient();
            String teamSequence = getTeamSequence(proceedingJoinPoint);
            String distlockKey = String.format(RedisKey.REDIS_DISTLOCK_PREFIX, teamSequence);
            rLock = redissonClient.getLock(distlockKey);
            rLock.lock(expireTime, TimeUnit.MILLISECONDS);
            start = System.currentTimeMillis();
            return proceedingJoinPoint.proceed();
        } finally {
            try {
                if (rLock != null && rLock.isLocked()) {
                    rLock.unlock();
                }
            } catch (Exception e) {
                logger.error("redis 分布式锁释放异常!", e);
            }
            String methodName = method.getName();
            logger.info(">> "+methodName+" cost "+(System.currentTimeMillis()-start)+" ms");
        }
    }

    /**
     * 获取切入点的方法
     * @param joinPoint
     * @return
     */
    private Method getPointMethod(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return methodSignature.getMethod();
    }


    /**
     * 通过方法参数获取车队序列号的值
     * @param joinPoint
     * @return
     */
    private String getTeamSequence(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        // 参数名
        String[] parameterNames = methodSignature.getParameterNames();
        // 参数类型
        Class[] parameterTypes = methodSignature.getParameterTypes();
        // 参数值对象
        Object[] parameterValues = joinPoint.getArgs();
        String teamSequence = null;
        if (ObjectTools.isNotEmpty(parameterValues)) {
            for (int i = 0; i < parameterNames.length; i++) {
                if ("sequence".equalsIgnoreCase(parameterNames[i])
                        && String.class.equals(parameterTypes[i])) {
                    teamSequence = parameterValues[i].toString();
                    break;
                } else {
                    teamSequence = reflectTeamSequence(parameterValues[i]);
                    if (ObjectTools.isEmpty(teamSequence)) {
                        continue;
                    }
                    break;
                }
            }
            if (ObjectTools.isNotEmpty(teamSequence)) {
                return teamSequence;
            }
        }
        throw new BusinessException(BizExceptionEnum.TEAM_NOT_EXIST);
    }

    /**
     * 反射获取 team sequence
     * @param param 方法上的参数对象
     * @return
     */
    private String reflectTeamSequence(Object param) {
        if (param == null) {
            return "";
        }
        // 1. 获取其从父类继承下来的所有字段( Object.class 排除)
        List<Field> fieldList = new ArrayList<>();
        Class clazz = param.getClass();
        while (clazz != null && !clazz.equals(Object.class)) {
            fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        // 2. 返回车队序列号的值
        for (Field field: fieldList) {
            field.setAccessible(true);
            // 字段名称
            String fieldName = field.getName();
            Object fieldValue;
            if (field.getType().equals(String.class)
                    && "sequence".equalsIgnoreCase(fieldName)) {
                try {
                    fieldValue = field.get(param);
                    if (ObjectTools.isNotEmpty(fieldValue)) {
                        return (String) fieldValue;
                    }
                } catch (IllegalAccessException e) {
                    // 已经设置了 accessible ...
                    logger.error("join pointer reflect get team sequence error!", e);
                }
                break;
            }
        }
        return "";
    }
}

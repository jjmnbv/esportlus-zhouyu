package com.kaihei.esportingplus.common.lock;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class RedisLockInterceptor implements Ordered {

    private CacheManager cacheManager = CacheManagerFactory.create();

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @SuppressWarnings("unchecked")
    private void handleObjectToParam(String prefix, Object o,
            Map<String, Object> paramMap) {

        boolean simpleValueType = BeanUtils.isSimpleValueType(o.getClass());
        if (simpleValueType) {
            paramMap.put(prefix, o);
            return;
        }

        if (o instanceof List) {
            Optional.ofNullable(o)
                    .map(it -> (List) it)
                    .filter(it -> !it.isEmpty())
                    .ifPresent(it -> it.forEach(
                            i -> handleObjectToParam(prefix + "." + it.indexOf(i), i,
                                    paramMap)));
            return;
        }

        if (!(o instanceof Map)) {
            o = JSON.parseObject(JSON.toJSONString(o));
        }
        handleMapToParam(prefix, (Map<String, Object>) o, paramMap);
    }

    private void handleMapToParam(String prefix, Map<String, Object> map,
            Map<String, Object> paramMap) {
        map.forEach((k, v) -> {
            String newPrefix = prefix + "." + k;
            handleObjectToParam(newPrefix, v, paramMap);
        });
    }

    @Around("@annotation(redisLock)")
    public Object around(ProceedingJoinPoint pjp, RedisLock redisLock) throws Throwable {
        //获取方法上的注解
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        HashMap<String, Object> map = Maps.newHashMap();

        String[] parameterNames = signature.getParameterNames();
        Object[] args = pjp.getArgs();

        IntStream.range(0, parameterNames.length).forEach(i -> map.put(parameterNames[i], args[i]));
        try {
            map.put("uid", UserSessionContext.getUser().getUid());
        } catch (Exception e) {
            //ignored
        }
        if (!StringUtils.isNotBlank(redisLock.uid())) {
            String uid = String.valueOf(ObjectTools.getParamByPath(redisLock.uid(), map));
            map.put("uid", uid);
        }
//        Map<String, Object> paramsMap = getParamsMap(map);
        //超时时间
        long expireTime = redisLock.expireTime();
        //Redis Key格式
        String key = redisLock.keyFormate();
        String[] split = key.split(":");

        for (String s : split) {
            if (s.startsWith("$.")) {
                String placeHolerName = s.replaceAll("\\$\\.", "");
                key = key
                        .replaceFirst("\\$\\." + placeHolerName,
                                String.valueOf(ObjectTools.getParamByPath(placeHolerName, map)));
            }
        }

        log.info("解析出的Redis 锁为 -> {}", key);

        RedissonClient redissonClient = cacheManager.redissonClient();

        RLock lock = redissonClient.getLock(key);
        long start = System.currentTimeMillis();
        try {
            lock = redissonClient.getLock(key);
            lock.lock(expireTime, TimeUnit.MILLISECONDS);
            return pjp.proceed();
        } finally {
            Thread currentThread = Thread.currentThread();
            if (!currentThread.isInterrupted()) {
                long tid = currentThread.getId();
                final RLock fl = lock;
                threadPoolTaskExecutor.submit(() -> fl.unlockAsync(tid));

                log.info(">> " + method.getName() + " cost " + (System.currentTimeMillis() - start)
                        + " ms");
            }

        }
    }

    /**
     * Get the order value of this object.
     * <p>Higher values are interpreted as lower priority. As a consequence,
     * the object with the lowest value has the highest priority (somewhat analogous to Servlet
     * {@code load-on-startup} values).
     * <p>Same order values will result in arbitrary sort positions for the
     * affected objects.
     *
     * @return the order value
     * @see #HIGHEST_PRECEDENCE
     * @see #LOWEST_PRECEDENCE
     */
    @Override
    public int getOrder() {
        return 3;
    }
}

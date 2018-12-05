package com.kaihei.esportingplus.resource.data.manager.aop;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.resource.data.manager.DictManager;
import com.kaihei.esportingplus.resource.data.manager.anno.DictCacheRemoveAll;
import com.kaihei.esportingplus.resource.data.manager.anno.DictCacheable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * @author 谢思勇
 * @date 2018年10月26日 12:11:48
 */
@Slf4j
@Aspect
@Component
@DependsOn("redisConfig")
public class DictCacheInterceptor implements InitializingBean {

    private final String REDIS_CACHE_PREFIX = "resource:cache:";
    private final String REDIS_CACHE_SURFIX = ":refresh";
    private final String RELOAD_MSG = "reload";


    private final org.springframework.cache.CacheManager cm = new ConcurrentMapCacheManager();
    private final Pattern placeHolderPatten = Pattern.compile("\\$\\{(\\w+)}");

    private final String redisCachePrefix = REDIS_CACHE_PREFIX;
    private final String redisCacheSurfix = REDIS_CACHE_SURFIX;
    private final String reloadMsg = RELOAD_MSG;

    private CacheManager cacheManager = CacheManagerFactory.create();
    @Autowired
    private List<DictManager> dictManagers;

    private Map<String, DictManager> suportDictManagerMap;

    @Around("execution(* com.kaihei.esportingplus.resource.data.manager.dao.*DAO.*(..))&&@annotation(anno)")
    public Object around(ProceedingJoinPoint pjp, DictCacheable anno) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        //缓存前缀是方法名
        String cachePrefix = signature.getMethod().getName();
        //缓存Key -> 方法名: 解析(注解value、参数名：参数值)
        String key = String.format("%s:%s", cachePrefix, resolveKey(pjp, anno));

        //被代理类命名规则： Model类名 + DAO
        //缓存名：Model类名 即 被代理类命名 [0,-3]
        String cacheName = getCacheName(pjp);

        Cache cache = cm.getCache(cacheName);
        ValueWrapper valueWrapper = cache.get(key);

        //缓存存在直接返回
        if (valueWrapper != null) {
            return valueWrapper.get();
        }

        //缓存不存在调用原方法 并缓存
        Object proceed = pjp.proceed();
        cache.put(key, proceed);

        return proceed;
    }

    @Around("execution(* com.kaihei.esportingplus.resource.data.manager.dao.*DAO.*(..))&&@annotation(anno)")
    public Object around(ProceedingJoinPoint pjp, DictCacheRemoveAll anno) throws Throwable {
        //执行目标方法
        Object proceed = pjp.proceed();
        //通过被代理类名 解析出缓存名
        String cacheName = getCacheName(pjp);

        //删除Redis上的缓存
        cacheManager.del(redisCachePrefix + cacheName);

        //通知其他节点需要重新加载数据
        cacheManager.redissonClient().getTopic(redisCachePrefix + cacheName + redisCacheSurfix)
                .publish(reloadMsg);
        return proceed;
    }

    /**
     * 根据切点 解析缓存名
     */
    private String getCacheName(ProceedingJoinPoint pjp) {
        String simpleName = pjp.getTarget().getClass().getSimpleName();
        return simpleName.substring(0, simpleName.length() - 3);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        suportDictManagerMap = dictManagers.stream().collect(Collectors.toMap(e -> {
            String typeName = e.getSuportType()
                    .getTypeName();
            return typeName
                    .substring(typeName.lastIndexOf(".") + 1);
        }, e -> e));

        //初始化Redis消息监听
        RedissonClient redissonClient = cacheManager.redissonClient();
        redissonClient.getPatternTopic(redisCachePrefix + "*" + redisCacheSurfix)
                /**
                 * @param channel = resource:cache: + cacheName + :refresh
                 * @param msg = reload
                 */
                .addListener((pattern, channel, msg) -> {
                    if (reloadMsg.equals(msg)) {
                        String cacheName = channel.toString()
                                .replaceAll(redisCachePrefix + "(.*)" + redisCacheSurfix, "$1");
                        log.info("收到 {} 刷新Cache消息 {}", cacheName, msg);
                        suportDictManagerMap.get(cacheName).resetReloadTag();

                        Cache cache = cm.getCache(cacheName);
                        cache.clear();
                    }
                });
    }

    private String resolveKey(ProceedingJoinPoint pjp, DictCacheable anno) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        //参数名 + 参数值解析成一个Map
        Object[] args = pjp.getArgs();
        String[] parameterNames = signature.getParameterNames();

        HashMap<String, Object> paramsMap = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            paramsMap.put(parameterNames[i], args[i]);
        }

        //根据定义的Key的格式替换其中的元素
        String keyFormat = anno.value();
        Matcher matcher = placeHolderPatten.matcher(keyFormat);
        while (matcher.find()) {
            String group = matcher.group(1);
            Object o = ObjectTools.getParamByPath(group, paramsMap);
            if (o != null) {
                String value;
                if (o.getClass().isArray()) {
                    value = Arrays.toString((Object[]) o);
                } else {
                    value = String.valueOf(o);
                }
                keyFormat = keyFormat.replaceAll("\\$\\{" + group + "}", value);
            }
        }
        return keyFormat;
    }

}

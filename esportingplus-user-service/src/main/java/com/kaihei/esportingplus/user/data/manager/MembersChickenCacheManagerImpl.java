package com.kaihei.esportingplus.user.data.manager;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.user.constant.UserRedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author xiekeqing
 * @Title: MembersChickenCacheManagerImpl
 * @Description: 用户鸡牌号缓存管理
 * @date 2018/9/1921:59
 */
@Component
public class MembersChickenCacheManagerImpl implements MembersChickenCacheManager{

    private static final Logger logger = LoggerFactory.getLogger(MembersChickenCacheManagerImpl.class);

    private static final CacheManager cacheManager = CacheManagerFactory.create();

    @Override
    public String getAvailableChickenId() {
        Long checkId = cacheManager
                .bitpos(UserRedisKey.USER_CHICKEN_AVAILABLE_KEY, false, 1250000, 12500000);
        cacheManager.setbit(UserRedisKey.USER_CHICKEN_AVAILABLE_KEY, checkId.longValue(), true);

        logger.debug("cmd=getAvailableChickenId >> chickid >> {}", checkId);
        return checkId.toString();
    }
}

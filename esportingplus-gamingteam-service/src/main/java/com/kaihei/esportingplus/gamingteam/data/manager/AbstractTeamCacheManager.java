package com.kaihei.esportingplus.gamingteam.data.manager;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.constant.RedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 车队 redis 操作
 *
 * @author liangyi
 */
public abstract class AbstractTeamCacheManager {

    protected static final Logger logger = LoggerFactory
            .getLogger(AbstractTeamCacheManager.class);
    protected static CacheManager cacheManager = CacheManagerFactory.create();

    /**
     * 生成车队 key
     * @param teamSequence
     * @return
     */
    protected String generateTeamKey (String teamSequence) {
        return String.format(RedisKey.TEAM_PREFIX, teamSequence);
    }
}

package com.kaihei.esportingplus.gamingteam.data.manager.cache;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.PVPFreeTeamMatchingVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PVPTeamQueueManager {

    private final String matchingTeamQueuePrefix = RedisKey.PVP_FREE_TEAM_MATCHING_TEAM_QUEUE;

    private CacheManager cacheManager = CacheManagerFactory.create();

    public void joinQueue(Integer settlementType, Integer teamTypeId, Integer gameZoneId,
            PVPFreeTeamMatchingVO pvpFreeTeamMatchingVO) {
        String key = getKey(settlementType, teamTypeId, gameZoneId);
        cacheManager.hset(key, pvpFreeTeamMatchingVO.getTeamSequence(), pvpFreeTeamMatchingVO);
    }

    public void quitQueue(Integer settlementType, Integer teamTypeId, Integer gameZoneId,
            String teamSequence) {
        String key = getKey(settlementType, teamTypeId, gameZoneId);
        cacheManager.hdel(key, teamSequence);
    }


    private String getKey(Integer settlementType, Integer teamTypeId, Integer gameZoneId) {
        return matchingTeamQueuePrefix
                + settlementType + ":" + teamTypeId + ":" + gameZoneId;
    }
}

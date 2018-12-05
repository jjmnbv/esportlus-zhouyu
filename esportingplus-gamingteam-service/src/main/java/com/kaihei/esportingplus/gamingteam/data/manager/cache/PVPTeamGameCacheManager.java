package com.kaihei.esportingplus.gamingteam.data.manager.cache;

import com.alibaba.fastjson.JSON;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.repository.CompositeRepository;
import com.kaihei.esportingplus.gamingteam.data.repository.TeamRepository;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PVPTeamGameCacheManager implements PVPCacheManager, Ordered {

    private static final String TEAM_GAME_PVP_REDIS_CACHE_PREFIX = "team:game:pvp:";
    private final String teamGamePvpRedisCachePrefix = TEAM_GAME_PVP_REDIS_CACHE_PREFIX;
    private CacheManager cacheManager = CacheManagerFactory.create();

    @Autowired
    private CompositeRepository compositeRepository;
    @Autowired
    private TeamRepository teamRepository;
    /**
     * 通过teamSequence移除缓存
     */
    @Override
    public void removeCache(String teamSequence) {
        String key = teamGamePvpRedisCachePrefix + teamSequence;
        log.info("移除Redis TG -> {}", key);
        cacheManager.del(key);
    }

    /**
     * 查询TeamGamePVP
     */
    public <T extends TeamGame> T getTeamGame(String teamSequence, Class<T> t) {
        String teamGameKey = teamGamePvpRedisCachePrefix + teamSequence;
        T teamGame = cacheManager.get(teamGameKey, t);
        log.info("\nteamGameKey-> {}\nteamGame-> {}", teamGameKey, teamGame);

        if (teamGame == null) {
            log.info("Redis查询失败，从数据查询");
            Team team = teamRepository.selectOne(Team.builder().sequence(teamSequence).build());
            if (team == null) {
                return null;
            }
            teamGame = BeanUtils.instantiate(t);
            teamGame.setTeamId(team.getId());
            teamGame = (T) compositeRepository.selectOne(teamGame);

        }

        return teamGame;
    }


    /**
     * TeamGamePVP缓存
     */
    public void storeTeamGame(String teamSequence, TeamGame teamGame) {
        storeCache("TeamGame", teamGamePvpRedisCachePrefix + teamSequence,
                teamGame);
    }

    /**
     * 缓存执行方法
     */
    private void storeCache(String cacheName, String key, Object cache) {
        String cacheStr = JSON.toJSONString(cache);
        log.info("缓存:{} key->{}", cacheName, key);
        cacheManager.set(key, cacheStr);
    }

    @Override
    public int getOrder() {
        return 10002;
    }
}

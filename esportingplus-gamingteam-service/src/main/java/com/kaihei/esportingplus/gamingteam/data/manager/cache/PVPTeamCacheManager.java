package com.kaihei.esportingplus.gamingteam.data.manager.cache;

import com.alibaba.fastjson.JSON;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPRedisTeamVO;
import com.kaihei.esportingplus.gamingteam.config.GamingTeamConfig;
import com.kaihei.esportingplus.gamingteam.data.repository.TeamGamePVPRepository;
import com.kaihei.esportingplus.gamingteam.data.repository.TeamRepository;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGamePVP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;


/**
 * PVP 车队 redis 操作
 * @author liangyi
 */
@Slf4j
@Service("pvpTeamCacheManager")
public class PVPTeamCacheManager implements PVPCacheManager, Ordered {

    private CacheManager cacheManager = CacheManagerFactory.create();

    private final String teamKeyFormat = RedisKey.TEAM_PREFIX;

    private static final String TEAM_REDIS_CACHE_PREFIX = "team:";

    private final String teamRedisCachePrefix = TEAM_REDIS_CACHE_PREFIX;

    @Autowired
    GamingTeamConfig gamingTeamConfig;

    @Autowired
    private TeamGamePVPRepository teamGamePVPRepository;

    @Autowired
    private TeamRepository teamRepository;

    public PVPRedisTeamVO queryTeamInfoBySequence(String sequence) {
        String teamKey = String.format(teamKeyFormat, sequence.trim());
        PVPRedisTeamVO redisTeamVO;
        redisTeamVO = cacheManager.get(teamKey, PVPRedisTeamVO.class);
        if (ObjectTools.isNull(redisTeamVO)) {
            log.warn(">> 未从 redis 中查询到车队[{}]的数据, 继续从 DB 中查询", sequence);
            Team team = teamRepository.selectOne(Team.builder().sequence(sequence).build());
            TeamGamePVP teamGamePVP = teamGamePVPRepository
                    .selectOne(TeamGamePVP.builder().teamId(team.getId()).build());
            PVPRedisTeamVO pvpRedisTeamVO = teamGamePVP.cast(PVPRedisTeamVO.class);
            team.cast(pvpRedisTeamVO);

            if (ObjectTools.isNull(redisTeamVO)) {
                log.error(">> 未从 DB 中查询到车队[{}]的数据", sequence);
                throw new BusinessException(BizExceptionEnum.TEAM_NOT_EXIST);
            }
            // 这里从数据库里查到的数据不要再加入到 redis 中了...
        }
        if (null == redisTeamVO.getPaymentTimeout()
                || 0 == redisTeamVO.getPaymentTimeout()) {
            // 由于数据库中没有存老板超时的字段
            // 所以这里需要从配置中读取
            redisTeamVO.setPaymentTimeout(gamingTeamConfig.getPaymentTimeout());
            //TODO 配置需要从其他地方获取
        }
        return redisTeamVO;
    }

    public Boolean teamExists(String teamSequence) {
        String teamKey = teamRedisCachePrefix + teamSequence;
        return cacheManager.exists(teamKey);
    }


    /**
     * 查询Team
     */
    public Team getTeam(String teamSequence) {
        String teamKey = teamRedisCachePrefix + teamSequence;
        Team team = cacheManager.get(teamKey, Team.class);
        log.info("\nteamCacheKey-> {}\nteam-> {}", teamKey, team);
        if (team == null) {
            team = teamRepository.selectOne(Team.builder().sequence(teamSequence).build());
            if (team == null) {
                throw new BusinessException(BizExceptionEnum.TEAM_NOT_EXIST);
            }
        }
        return team;
    }


    /**
     * Team缓存
     */
    public void storeTeam(Team team) {
        storeCache("Team", teamRedisCachePrefix + team.getSequence(), team);
    }


    /**
     * 缓存执行方法
     */
    private void storeCache(String cacheName, String key, Object cache) {
        String cacheStr = JSON.toJSONString(cache);
        log.info("\n缓存:{}\nkey->{}\ncache->{}", cacheName, key, cacheStr);
        cacheManager.set(key, cacheStr);
    }

    /**
     * 通过teamSequence移除缓存
     */
    @Override
    public void removeCache(String teamSequence) {
        String key = teamRedisCachePrefix + teamSequence;
        log.info("移除Redis T -> {}", key);
        cacheManager.del(key);
    }

    @Override
    public int getOrder() {
        return 10001;
    }
}

package com.kaihei.esportingplus.gamingteam.data.manager.cache;

import com.alibaba.fastjson.JSON;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.gamingteam.api.enums.PVPTeamMemberStatusEnum;
import com.kaihei.esportingplus.gamingteam.data.manager.AbstractTeamMemberCacheManager;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.BaseTeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.BaseTeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.repository.CompositeRepository;
import com.kaihei.esportingplus.gamingteam.data.repository.TeamRepository;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

/**
 * PVP 车队队员 redis 操作
 *
 * @author liangyi
 */
@Slf4j
@Service("pvpTeamMemberCacheManager")
public class PVPTeamMemberCacheManager extends AbstractTeamMemberCacheManager implements
        PVPCacheManager, Ordered {

    private CacheManager cacheManager = CacheManagerFactory.create();

    private static final String TEAM_MEMBER_REDIS_CACHE_PREFIX = "team:member:";

    private final String teamMemberRedisCachePrefix = TEAM_MEMBER_REDIS_CACHE_PREFIX;

    @Autowired
    private PVPTeamGameCacheManager pvpTeamGameCacheManager;

    @Autowired
    private CompositeRepository compositeRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PVPTeamCacheManager pvpTeamCacheManager;

    /**
     * 获取TeamMemberPVP
     */
    public <T extends TeamMember> T getTeamMember(String teamSequence,
            String memberUid, Class<T> t) {
        String teamMemberKey = teamMemberRedisCachePrefix + teamSequence;
        T teamMemberPVP = cacheManager
                .hget(teamMemberKey, memberUid, t);
        log.info("teamMemberKey-> {} ", teamMemberKey);
        if (teamMemberPVP == null) {
            throw new BusinessException(BizExceptionEnum.TEAM_CURRENT_MEMBER_HAS_LEAVED);
        }
        return teamMemberPVP;
    }


    /**
     * 判断某车队存在某用户
     */
    public boolean existsTeamMember(String teamSequence,
            String memberUid) {
        String teamMemberKey = teamMemberRedisCachePrefix + teamSequence;
        return cacheManager
                .hexists(teamMemberKey, memberUid);
    }

    /**
     * 获取TeamMemberPVP
     */
    public <T extends TeamMember> List<T> getTeamMemberList(String teamSequence, Class<T> t) {
        String teamMemberKey = teamMemberRedisCachePrefix + teamSequence;
        Map<String, T> teamMemberPVPMap = cacheManager
                .hgetAll(teamMemberKey, t);
        List<T> teamMemberPVPArrayList = new ArrayList<>(
                teamMemberPVPMap.values());
        log.info("\nteamMemberKey-> {}\nteamMember-> {}", teamMemberKey, teamMemberPVPArrayList);

        if (teamMemberPVPArrayList == null || teamMemberPVPArrayList.isEmpty()) {
            Team team = teamRepository.selectOne(Team.builder().sequence(teamSequence).build());
            T tm = BeanUtils.instantiate(t);
            tm.setTeamId(team.getId());
            teamMemberPVPArrayList = compositeRepository.select(tm);
        }
        return teamMemberPVPArrayList;
    }

    /**
     * 查询用户所在的车队的序列号
     */
    public String findUserTeamSequence(String uid, Integer gameId) {
        String key = generateTeamMemberShardingKey(uid, gameId);
        return cacheManager.hget(key, uid, String.class);
    }

    /**
     * 返回true正常存储
     *
     * 返回false异常流
     */
    public boolean storeTeamMember(TeamMember joinTeamMember, Integer gameId,
            String teamSequence) {
        String uid = joinTeamMember.getUid();
        String key = generateTeamMemberShardingKey(uid, gameId);
        //上车
        cacheManager.hset(key, uid, teamSequence);
        log.info("缓存:TeamMember key->{} cache->{}", key, uid + ":" + teamSequence);
        //存用户信息
        String cacheStr = JSON.toJSONString(joinTeamMember);
        cacheManager.hset(teamMemberRedisCachePrefix + teamSequence, uid, cacheStr);
        return true;
    }

    public void restoreTeamMember(TeamMember joinTeamMember, String teamSequence) {
        String uid = joinTeamMember.getUid();
        String cacheStr = JSON.toJSONString(joinTeamMember);

        cacheManager.hset(teamMemberRedisCachePrefix + teamSequence, uid, cacheStr);
    }

    public void restoreTeamMember(TeamMember joinTeamMember, Integer gameId, String teamSequence) {
        //开车后离开
        String uid = joinTeamMember.getUid();
        String userKey = generateTeamMemberShardingKey(joinTeamMember.getUid(), gameId);
        if (PVPTeamMemberStatusEnum.TEAM_START_QUIT
                .equals(PVPTeamMemberStatusEnum.of(joinTeamMember.getStatus()))) {
            cacheManager.hdel(userKey, uid);
        }
        //重新缓存时用户占位信息不存在
        else if (!cacheManager.hexists(userKey, uid)) {
            cacheManager.hset(userKey, uid, teamSequence);
        }
        restoreTeamMember(joinTeamMember, teamSequence);
    }


    public void removeTeamMemberFromTeam(String teamSequence, String uid, Integer gameId) {
        String userKey = generateTeamMemberShardingKey(uid, gameId);
        String teamMemberKey = teamMemberRedisCachePrefix + teamSequence;
        log.info("移除用户->{}", uid);
        cacheManager.hdel(userKey, uid);
        cacheManager.hdel(teamMemberKey, uid);
    }

    /**
     * 通过teamSequence移除缓存
     */
    @Override
    public void removeCache(String teamSequence) {
        BaseTeamGame baseTeamGame = pvpTeamGameCacheManager
                .getTeamGame(teamSequence, BaseTeamGame.class);
        if (baseTeamGame == null) {
            return;
        }
        Integer gameId = baseTeamGame.getGameId();

        String teamMemberKey = teamMemberRedisCachePrefix + teamSequence;

        Map<String, BaseTeamMember> uidTeamMemberMap = cacheManager
                .hgetAll(teamMemberKey, BaseTeamMember.class);

        uidTeamMemberMap.forEach((uid, tm) -> {
            String userKey = generateTeamMemberShardingKey(uid, gameId);
            log.info("移除Redis用户占位 -> {} , uid {}", userKey, uid);
            cacheManager.hdel(userKey, uid);
        });

        log.info("移除Redis TM -> {}", teamMemberKey);
        cacheManager.del(teamMemberKey);
    }

    @Override
    public int getOrder() {
        return 10000;
    }
}

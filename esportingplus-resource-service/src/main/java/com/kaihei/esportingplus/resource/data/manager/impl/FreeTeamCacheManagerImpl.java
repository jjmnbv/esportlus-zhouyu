package com.kaihei.esportingplus.resource.data.manager.impl;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.api.vo.BaojiDanRangeVO;
import com.kaihei.esportingplus.api.vo.freeteam.DanDictVo;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamTypeDetailVO;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.resource.data.manager.FreeTeamCacheManager;
import com.kaihei.esportingplus.resource.data.repository.BaojiDanRangeRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Pipeline;

/**
 * @author liangyi
 */
@Component
public class FreeTeamCacheManagerImpl implements FreeTeamCacheManager {

    private static final Logger log = LoggerFactory.getLogger(FreeTeamCacheManagerImpl.class);

    protected static CacheManager cacheManager = CacheManagerFactory.create();

    @Autowired
    BaojiDanRangeRepository baojiDanRangeRepository;

    /** redis key 模糊匹配字符 */
    private static final String ALL_KEYS_PATTERN = "*";

    @Override
    public void addOrUpdateFreeTeamType(FreeTeamTypeDetailVO teamTypeDetailVO) {
        Pipeline pipelined = cacheManager.pipelined();
        String key = getFreeTeamTypeKey(teamTypeDetailVO.getFreeTeamTypeId());
        pipelined.set(key, JacksonUtils.toJsonWithSnake(teamTypeDetailVO));
        List<DanDictVo> gameDanList = teamTypeDetailVO.getGameDanList();
        if (ObjectTools.isNotEmpty(gameDanList)) {
            for (DanDictVo gameDan : gameDanList) {
                String freeTeamTypeDanKey = getFreeTeamTypeDanKey(
                        teamTypeDetailVO.getFreeTeamTypeId(), gameDan.getDictId());
                pipelined.set(freeTeamTypeDanKey, JacksonUtils.toJsonWithSnake(gameDan));
            }
        }
        pipelined.sync();
        log.info("将免费车队类型:[{}]放入redis中", key);
    }

    @Override
    public void deleteFreeTeamType(Integer freeTeamTypeId) {
        String freeTeamTypeKey = getFreeTeamTypeKey(freeTeamTypeId);
        // 先拿到所有相关的段位 key
        /*
         * 这里因为数据存储类型定义的问题, 先用 keys 进行通配
         * 这个 key 不会很多, 而且修改不会很频繁, 待游戏配置重构后统一修改 todo
         */
        Iterable<String> danKeys = cacheManager.redissonClient().getKeys()
                .getKeysByPattern(freeTeamTypeKey + RedisKey.KEY_SEPARATOR + ALL_KEYS_PATTERN);
        Pipeline pipelined = cacheManager.pipelined();
        for (String danKey : danKeys) {
            // 删除段位 key
            log.info("删除redis中的免费车队类型, 当前免费车队段位key:[{}]", danKey);
            pipelined.del(danKey);
        }
        // 删除免费车队类型 key
        pipelined.del(freeTeamTypeKey);
        pipelined.sync();
        log.info("删除redis中的免费车队类型:[{}]", freeTeamTypeKey);
    }

    @Override
    public void addOrUpdateBaojiDanRange(BaojiDanRangeVO baojiDanRangeVO) {
        if (ObjectTools.isNotEmpty(baojiDanRangeVO)) {
            Integer gameId = baojiDanRangeVO.getGame().getDictId();
            String baojiDanRangeKey = getBaojiDanRangeKey(gameId);
            // 存 hash 结构, field为暴鸡等级code
            cacheManager.hset(baojiDanRangeKey, baojiDanRangeVO.getBaojiLevel()+"",
                    JacksonUtils.toJsonWithSnake(baojiDanRangeVO));
        }
        log.info("将暴鸡接单范围:[{}]放入redis中", baojiDanRangeVO);
    }

    @Override
    public void deleteBaojiDanRange(Integer gameId, Integer baojiLevel) {
        String baojiDanRangeKey = getBaojiDanRangeKey(gameId);
        cacheManager.hdel(baojiDanRangeKey, baojiLevel+"");
        log.info("删除redis中的暴鸡接单范围, 游戏id:[{}], 暴鸡等级:[{}]", gameId, baojiLevel);
    }

    @Override
    public BaojiDanRangeVO getBaojiDanRange(Integer gameId, Integer baojiLevel) {
        String baojiDanRangeKey = getBaojiDanRangeKey(gameId);
        BaojiDanRangeVO baojiDanRangeVO = cacheManager
                .hget(baojiDanRangeKey, baojiLevel+"", BaojiDanRangeVO.class);
        return baojiDanRangeVO;
    }

    private String getFreeTeamTypeKey(Integer freeTeamTypeId) {
        return String.format(RedisKey.FREE_TEAM_TYPE_ID, freeTeamTypeId);
    }

    private String getFreeTeamTypeDanKey(Integer freeTeamTypeId, Integer gameDanId) {
        return String.format(RedisKey.FREE_TEAM_TYPE_DAN, freeTeamTypeId, gameDanId);
    }

    private String getBaojiDanRangeKey(Integer gameId) {
        return String.format(RedisKey.BAOJI_DAN_RANGE_GAME, gameId);
    }

}

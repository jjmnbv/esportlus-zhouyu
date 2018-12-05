package com.kaihei.esportingplus.gamingteam.data.manager;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.api.feign.ResourceServiceClient;
import com.kaihei.esportingplus.api.vo.RedisGame;
import com.kaihei.esportingplus.api.vo.RedisGameRaid;
import com.kaihei.esportingplus.api.vo.RedisSmallZoneRefAcrossZone;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Pipeline;

/**
 * @author liangyi
 */
@Service("gameConfigService")
public class GameConfigServiceImpl implements GameConfigService, ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(GameConfigServiceImpl.class);

    protected static CacheManager cacheManager = CacheManagerFactory.create();

    @Autowired
    ResourceServiceClient resourceServiceClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //预热游戏列表
        this.preheatGameList();

    }

    private void preheatGameList() {
        // 热备游戏列表
        ResponsePacket<List<RedisGame>> gameListResp = resourceServiceClient.getGameList();
        if (gameListResp.responseSuccess()) {
            List<RedisGame> gameList = gameListResp.getData();
            Pipeline pipelined = cacheManager.pipelined();
            for (RedisGame redisGame : gameList) {
                if (ObjectTools.allFieldNotNull(redisGame)) {
                    Integer gameCode = redisGame.getCode();
                    String gameStr = JacksonUtils.toJson(redisGame);
                    if (ObjectTools.isNotEmpty(gameStr)) {
                        pipelined.hset(RedisKey.TEAM_GAME_LIST_KEY,
                                ObjectTools.covertToString(gameCode), gameStr);
                    }
                }
            }
            // 放入缓存
            pipelined.sync();
        } else {
            logger.error(">> 调用资源服务获取游戏列表错误: {}", gameListResp);
            throw new BusinessException(gameListResp.getCode(), gameListResp.getMsg());
        }
    }

    @Override
    public RedisSmallZoneRefAcrossZone getBigAndAcrossZoneBySmallZoneCode(Integer gameCode,
            Integer smallZoneCode) {
        String redisKey = String
                .format(RedisKey.TEAM_SMALL_ZONE_REF_ACROSS_ZONE_KEY, gameCode, smallZoneCode);
        RedisSmallZoneRefAcrossZone zone = cacheManager
                .get(redisKey,
                        RedisSmallZoneRefAcrossZone.class);
        if (ObjectTools.isNotEmpty(zone)) {
            return zone;
        }
        //如果在缓存中找不到，调用接口
        ResponsePacket<RedisSmallZoneRefAcrossZone> acrossFromSmallResp =
                resourceServiceClient.getAcrossZoneFromSmallZoneCode(gameCode,smallZoneCode);
        if (acrossFromSmallResp.responseSuccess()) {
            RedisSmallZoneRefAcrossZone data = acrossFromSmallResp.getData();
            ValidateAssert.hasNotNull(BizExceptionEnum.ZONE_SERVER_NOT_MATCH, data);
            //如果获取到了存入redis
            cacheManager.set(redisKey, JacksonUtils.toJson(data));
            return data;
        } else {
            logger.error(">> 调用资源服务获取跨区信息错误: {}", acrossFromSmallResp);
            throw new BusinessException(acrossFromSmallResp.getCode(),
                    acrossFromSmallResp.getMsg());
        }
    }

    @Override
    public List<RedisGameRaid> getGameRaidList(Integer gameCode) {

        // 先从缓存中获取
        String gameRaidKey = String.format(RedisKey.TEAM_GAME_RAID_LIST_KEY, gameCode);
        Map<String, RedisGameRaid> redisGameRaidMap = cacheManager
                .hgetAll(gameRaidKey, RedisGameRaid.class);
        if (ObjectTools.isNotEmpty(redisGameRaidMap)) {
            List<RedisGameRaid> raidList = new ArrayList<>(redisGameRaidMap.values());
            if (raidList.size() > 0) {
                return raidList;
            }
        }
        // 缓存中获取不到再调用接口
        ResponsePacket<List<RedisGameRaid>> gameRaidsResp = resourceServiceClient
                .getGameRaids(gameCode);
        if (gameRaidsResp.responseSuccess()) {
            List<RedisGameRaid> gameRaidList = gameRaidsResp.getData();
            Pipeline pipelined = cacheManager.pipelined();
            for (RedisGameRaid redisGameRaid : gameRaidList) {
                if (ObjectTools.allFieldNotNull(redisGameRaid)) {
                    Integer raidCode = redisGameRaid.getRaidCode();
                    String gameRaidStr = JacksonUtils.toJson(redisGameRaid);
                    if (ObjectTools.isNotEmpty(gameRaidStr)) {
                        pipelined.hset(gameRaidKey,
                                ObjectTools.covertToString(raidCode), gameRaidStr);
                    }
                }
            }
            // 放入缓存
            pipelined.sync();
            return gameRaidList;
        } else {
            logger.error(">> 调用资源服务获取游戏副本列表错误: {}", gameRaidsResp);
            throw new BusinessException(gameRaidsResp.getCode(), gameRaidsResp.getMsg());
        }
    }

    @Override
    public RedisGameRaid getGameRaid(Integer gameCode, Integer raidCode) {
        // 先从缓存中获取
        String gameRaidKey = String.format(RedisKey.TEAM_GAME_RAID_LIST_KEY, gameCode);
        RedisGameRaid redisGameRaid = cacheManager
                .hget(gameRaidKey, ObjectTools.covertToString(raidCode), RedisGameRaid.class);
        if (ObjectTools.isNotEmpty(redisGameRaid)) {
            return redisGameRaid;
        }
        // 获取不到再调接口获取
        ResponsePacket<RedisGameRaid> gameRaidResp = resourceServiceClient
                .getSingleGameRaid(gameCode, raidCode);
        if (gameRaidResp.responseSuccess()) {
            RedisGameRaid data = gameRaidResp.getData();
            if (ObjectTools.allFieldNotNull(data)) {
                Integer dataRaidCode = data.getRaidCode();
                String dataStr = JacksonUtils.toJson(data);
                if (ObjectTools.isNotEmpty(dataStr)) {
                    // 放入缓存
                    cacheManager.hset(gameRaidKey, ObjectTools.covertToString(dataRaidCode),
                            JacksonUtils.toJson(data));
                    return data;
                }
            }
            throw new BusinessException(BizExceptionEnum.GAME_RAID_NOT_EXIST);
        } else {
            logger.error(">> 调用资源服务获取单个游戏副本错误: {}", gameRaidResp);
            throw new BusinessException(gameRaidResp.getCode(), gameRaidResp.getMsg());
        }
    }
}

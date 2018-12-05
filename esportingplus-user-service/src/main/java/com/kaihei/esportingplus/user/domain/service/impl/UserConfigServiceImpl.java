package com.kaihei.esportingplus.user.domain.service.impl;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.api.feign.ResourceServiceClient;
import com.kaihei.esportingplus.api.vo.RedisGameRaid;
import com.kaihei.esportingplus.api.vo.RedisGameSmallZone;
import com.kaihei.esportingplus.api.vo.RedisSmallZoneRefAcrossZone;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.user.api.vo.UserGameDetailRoleInfoVo;
import com.kaihei.esportingplus.user.domain.service.UserConfigService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Pipeline;

/**
 * @author zhangfang
 */
@Service
public class UserConfigServiceImpl implements UserConfigService {

    private static final Logger logger = LoggerFactory.getLogger(UserConfigServiceImpl.class);

    protected static CacheManager cacheManager = CacheManagerFactory.create();
    @Autowired
    ResourceServiceClient resourceServiceClient;

    @Override
    public List<RedisGameRaid> getGameRaidList(Integer gameCode) {

        // 先从缓存中获取
        String gameRaidKey = String.format(RedisKey.USER_GAME_RAID_LIST_KEY, gameCode);
        Map<String, RedisGameRaid> redisGameRaidMap = cacheManager
                .hgetAll(gameRaidKey, RedisGameRaid.class);
        if (ObjectTools.isNotEmpty(redisGameRaidMap)) {
            List<RedisGameRaid> raidList = new ArrayList<>(redisGameRaidMap.values());
            if (raidList.size() > 0) {
                return raidList;
            }
        }
        // 缓存中获取不到再调用接口
        ResponsePacket<List<RedisGameRaid>> gameRaids = resourceServiceClient
                .getGameRaids(gameCode);
        if (gameRaids.responseSuccess()) {
            List<RedisGameRaid> gameRaidList = gameRaids.getData();
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
            logger.error(">> resourceServiceClient.getGameList() error!", gameRaids.getCode(),
                    gameRaids.getMsg());
            throw new BusinessException(gameRaids.getCode(), gameRaids.getMsg());
        }
    }

    @Override
    public RedisGameRaid getGameRaid(Integer gameCode, Integer raidCode) {
        // 先从缓存中获取
        String gameRaidKey = String.format(RedisKey.USER_GAME_RAID_LIST_KEY, gameCode);
        RedisGameRaid redisGameRaid = cacheManager
                .hget(gameRaidKey, ObjectTools.covertToString(raidCode), RedisGameRaid.class);
        if (ObjectTools.isNotEmpty(redisGameRaid)) {
            return redisGameRaid;
        }
        // 获取不到再调接口获取
        ResponsePacket<RedisGameRaid> singleGameRaid = resourceServiceClient
                .getSingleGameRaid(gameCode, raidCode);
        if (singleGameRaid.responseSuccess()) {
            RedisGameRaid data = singleGameRaid.getData();
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
            logger.error(">> resourceServiceClient.getGameList() error!", singleGameRaid.getCode(),
                    singleGameRaid.getMsg());
            throw new BusinessException(singleGameRaid.getCode(), singleGameRaid.getMsg());
        }
    }

    @Override
    public RedisSmallZoneRefAcrossZone getBigAndAcrossZoneBySmallCode(Integer gameCode,
            Integer smallZoneCode) {
        String redisKey = String
                .format(RedisKey.USER_SMALL_ZONE_REF_ACROSS_ZONE_KEY, gameCode, smallZoneCode);
        RedisSmallZoneRefAcrossZone zone = cacheManager
                .get(redisKey,
                        RedisSmallZoneRefAcrossZone.class);
        if (ObjectTools.isNotEmpty(zone)) {
            return zone;
        }
        //如果在缓存中找不到，调用接口
        ResponsePacket<RedisSmallZoneRefAcrossZone> responsePacket = resourceServiceClient
                .getAcrossZoneFromSmallZoneCode(gameCode,
                        smallZoneCode);
        if (responsePacket.responseSuccess()) {
            RedisSmallZoneRefAcrossZone data = responsePacket.getData();
            ValidateAssert.hasNotNull(BizExceptionEnum.ZONE_SERVER_NOT_MATCH, data);
            //如果获取到了存入redis
            cacheManager.set(redisKey, JacksonUtils.toJson(data));
            return data;
        } else {
            logger.error(
                    ">> resourceServiceClient.getAcrossZoneFromSmallZoneCode() error!,code:{},msg:{}",
                    responsePacket.getCode(),
                    responsePacket.getMsg());
            throw new BusinessException(responsePacket.getCode(), responsePacket.getMsg());
        }
    }

    @Override
    public List<RedisGameRaid> getGameRaidThroughCertCode(Integer gameCode, Integer certRaidCode) {
        String redisKey = String
                .format(RedisKey.USER_CERT_RAID_REF_TYPE_RAID_KEY, gameCode, certRaidCode);
        String result = cacheManager
                .get(redisKey,
                        String.class);
        List<RedisGameRaid> list = null;
        if (ObjectTools.isNotEmpty(result)) {
            list = (List<RedisGameRaid>) JacksonUtils
                    .toBeanCollection(result, List.class, RedisGameRaid.class);
            return list;
        }
        //如果没有找到，则从resource里面去寻找
        ResponsePacket<List<RedisGameRaid>> responsePacket = resourceServiceClient
                .getGameRaidThroughCertCode(gameCode, certRaidCode);
        if (responsePacket.responseSuccess()) {
            List<RedisGameRaid> data = responsePacket.getData();
            ValidateAssert.hasNotNull(BizExceptionEnum.RAID_CERT_NOT_EXIST_RAID, data);
            //如果获取到了存入redis
            cacheManager.set(redisKey, JacksonUtils.toJson(data));
            return data;
        } else {
            logger.error(
                    ">> resourceServiceClient.getGameRaidThroughCertCode() error!,code:{},msg:{}",
                    responsePacket.getCode(),
                    responsePacket.getMsg());
            throw new BusinessException(responsePacket.getCode(), responsePacket.getMsg());
        }
    }

    @Override
    public String getSmallZoneName(Integer gameCode, Integer smallZoneCode) {
        String redisKey = String.format(RedisKey.USER_SMALL_ZONE_INFO_KEY, gameCode, smallZoneCode);
        String smallZoneName = cacheManager
                .get(redisKey, String.class);
        if (ObjectTools.isNotEmpty(smallZoneName)) {
            return smallZoneName;
        }
        ResponsePacket<String> responsePacket = resourceServiceClient
                .getSmallZoneName(gameCode, smallZoneCode);
        if (responsePacket.responseSuccess()) {
            String data = responsePacket.getData();
            ValidateAssert.hasNotNull(BizExceptionEnum.ZONE_SERVER_NOT_EXIST, data);
            //如果获取到了存入redis
            cacheManager.set(redisKey, data);
            return data;
        } else {
            logger.error(
                    ">> resourceServiceClient.getSmallZoneName() error!,code:{},msg:{}",
                    responsePacket.getCode(),
                    responsePacket.getMsg());
            throw new BusinessException(responsePacket.getCode(), responsePacket.getMsg());
        }

    }
    @Override
    public UserGameDetailRoleInfoVo getUserRoleDetailRoleInfo(String userRoleKey,String roleId){
        return cacheManager
                .hget(userRoleKey,
                        roleId, UserGameDetailRoleInfoVo.class);
    }

    @Override
    public  Map<String, UserGameDetailRoleInfoVo> getUserAllRoleDetailRoleInfo(String userRoleKey) {
        return cacheManager
                .hgetAll(userRoleKey, UserGameDetailRoleInfoVo.class);
    }

    @Override
    public List<Integer> findSmallCodeFromAcrossCode(Integer gameCode, Integer zoneAcrossCode) {
        ResponsePacket<List<RedisGameSmallZone>> response = resourceServiceClient
                .getSmallZoneByAcrossCode(gameCode, zoneAcrossCode);
        if(!response.responseSuccess()){
            throw new BusinessException(response.getCode(),response.getMsg());
        }
        List<RedisGameSmallZone> data = response.getData();
        ValidateAssert.isTrue(ObjectTools.isNotEmpty(data), BizExceptionEnum.ZONE_SERVER_NOT_EXIST);
        //获取所有小区
        return data.stream()
                .map(m -> m.getZoneSmallCode())
                .collect(Collectors.toList());
    }
}

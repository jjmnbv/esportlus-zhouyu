package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.vo.BaojiLevelRateVo;
import com.kaihei.esportingplus.api.vo.BaseGameRaidVo;
import com.kaihei.esportingplus.api.vo.FrontTopCareer;
import com.kaihei.esportingplus.api.vo.RaidAndGameServerVo;
import com.kaihei.esportingplus.api.vo.RedisGame;
import com.kaihei.esportingplus.api.vo.RedisGameAcrossZone;
import com.kaihei.esportingplus.api.vo.RedisGameBigZone;
import com.kaihei.esportingplus.api.vo.RedisGameRaid;
import com.kaihei.esportingplus.api.vo.RedisGameSmallZone;
import com.kaihei.esportingplus.api.vo.RedisSmallZoneRefAcrossZone;
import com.kaihei.esportingplus.api.vo.RedisTopCareer;
import com.kaihei.esportingplus.api.vo.ResourceVO;
import com.kaihei.esportingplus.api.vo.SimpleGameRaid;
import com.kaihei.esportingplus.common.ResponsePacket;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 基于feign实现远程车队服务接口调用<br/> 1. esportingplus-resource-service为服务名 <br/> 2.
 * fallbackFactory指定断路器实现类<br/>
 */
@FeignClient(name = "esportingplus-resource-service", path = "/resources", fallbackFactory = ResourceClientFallbackFactory.class)
public interface ResourceServiceClient {

    @GetMapping("/{id}")
    ResponsePacket<ResourceVO> getResourceById(@PathVariable("id") String id);

    /**
     * 从小区获取对应跨区
     */
    @GetMapping("/game/{game_code}/small_zone/{small_zone_code}/exchange_across")
    public ResponsePacket<RedisSmallZoneRefAcrossZone> getAcrossZoneFromSmallZoneCode(
            @PathVariable("game_code") Integer gameCode,
            @PathVariable("small_zone_code") Integer smallZoneCode);

    /**
     * 从跨区获取对应的大区小区
     */
    @GetMapping("/game/{game_code}/across_zone/{zone_across_code}/exchange_big_small")
    public ResponsePacket<List<RedisGameBigZone>> getBigOrSmallZoneByAcrossCode(
            @RequestHeader("Authorization") String token,
            @PathVariable("game_code") Integer gameCode,
            @PathVariable("zone_across_code") Integer zoneAcrossCode);

    /**
     * 通过跨区code找到小区
     * @param gameCode
     * @param zoneAcrossCode
     * @return
     */
    @GetMapping("/game/{game_code}/across_zone/{zone_across_code}/exchange_small_small")
    public ResponsePacket<List<RedisGameSmallZone>> getSmallZoneByAcrossCode( @PathVariable("game_code") Integer gameCode,
            @PathVariable("zone_across_code") Integer zoneAcrossCode);

    /**
     * 通关大区code找到小区列表
     */
    @GetMapping("/game/{game_code}/big_zone/{zone_big_code}/exchange_small")
    public ResponsePacket<List<RedisGameSmallZone>> getSmallZoneByBigCode(
            @PathVariable("game_code") Integer gameCode,
            @PathVariable("zone_big_code") Integer zoneBigCode);

    /**
     * 通关大区code找到大区和小区列表
     */
    @GetMapping("/game/{game_code}/big_zone/{zone_big_code}/exchange_big")
    public ResponsePacket<RedisGameBigZone> getBigAndSmallZoneByBigCode(
            @PathVariable("game_code") Integer gameCode,
            @PathVariable("zone_big_code") Integer zoneBigCode);

    /**
     * 获取游戏职业列表
     */
    @GetMapping("/game/{game_code}/career")
    public ResponsePacket<List<RedisTopCareer>> getCareerByGameCode(
            @PathVariable("game_code") Integer gameCode);

    /**
     * 获取游戏职业列表
     */
    @GetMapping("/game/app/{game_code}/career")
    public ResponsePacket<List<FrontTopCareer>> getCareerByGameCodeForApp(
            @RequestHeader("Authorization") String token,
            @PathVariable("game_code") Integer gameCode);

    /**
     * 获取游戏某个顶级下的职业
     */
    @GetMapping("/game/{game_code}/career/{career_code}")
    public ResponsePacket<RedisTopCareer> getCareerByGameCodeAndTopCareerCode(
            @PathVariable("game_code") Integer gameCode,
            @PathVariable("career_code") Integer careerCode);

    /**
     * 获取游戏列表
     */
    @GetMapping("/game/list")
    public ResponsePacket<List<RedisGame>> getGameList();

    /**
     * 获取游戏列表
     */
    @GetMapping("/game/app/list")
    public ResponsePacket<List<RedisGame>> getGameListForApp(
            @RequestHeader("Authorization") String token);

    /**
     * 获取游戏副本列表
     */
    @GetMapping("/game/{game_code}/raid")
    public ResponsePacket<List<RedisGameRaid>> getGameRaids(
            @PathVariable("game_code") Integer gameCode);

    /**
     * APP调用-获取副本列表
     */
    @GetMapping("/game/app/{game_code}/raid")
    public ResponsePacket<List<SimpleGameRaid>> getGameRaidsApp(
            @RequestHeader("Authorization") String token,
            @PathVariable("game_code") Integer gameCode);

    /**
     * 获取单个游戏副本
     */
    @GetMapping("/game/{game_code}/raid/{raid_code}")
    public ResponsePacket<RedisGameRaid> getSingleGameRaid(
            @PathVariable("game_code") Integer gameCode,
            @PathVariable("raid_code") Integer raidCode);

    /**
     * 获取认证副本列表
     */
    @GetMapping("/game/{game_code}/cert_raid/list")
    public ResponsePacket<List<BaseGameRaidVo>> getCertGameRaids(
            @PathVariable("game_code") Integer gameCode);

    /**
     * 根据游戏code和认证副本code获取认证副本信息
     */
    @GetMapping("/game/{game_code}/cert_raid/{cert_raid_code}")
    public ResponsePacket<BaseGameRaidVo> getCertSingleGameRaid(
            @PathVariable("game_code") Integer gameCode,
            @PathVariable("cert_raid_code") Integer certRaidCode);

    /**
     * 通过游戏code和认证副本找到下面所属副本
     */
    @GetMapping("/game/{game_code}/raid/cert_raid/{cert_raid_code}")
    public ResponsePacket<List<RedisGameRaid>> getGameRaidThroughCertCode(
            @PathVariable("game_code") Integer gameCode,
            @PathVariable("cert_raid_code") Integer certRaidCode);

    /**
     * 获取跨区列表
     */
    @GetMapping("/game/{game_code}/zone/across")
    public ResponsePacket<List<RedisGameAcrossZone>> getGameAcrossZone(
            @PathVariable("game_code") Integer gameCode);

    /**
     * 获取跨区列表
     */
    @GetMapping("/game/app/{game_code}/zone/across")
    public ResponsePacket<List<RedisGameAcrossZone>> getGameAcrossZoneForApp(
            @RequestHeader("Authorization") String token,
            @PathVariable("game_code") Integer gameCode);

    /**
     * 获取暴鸡等级对应的收益系数
     */
    @GetMapping("/baoji/level/{baoji_level}/rate")
    public ResponsePacket<BigDecimal> getBaojiLevelRate(
            @PathVariable("baoji_level") Integer baojiLevel);

    /**
     * 批量获取暴鸡等级对应的收益系数
     */
    @PostMapping("/baoji/level/rate/batch")
    public ResponsePacket<List<BaojiLevelRateVo>> getBaojiLevelRateBatch(
            @RequestBody List<Integer> params);

    /**
     * 获取游戏大小区
     */
    @GetMapping("/game/{game_code}/big_small_zone/list")
    public ResponsePacket<List<RedisGameBigZone>> getBigZoneAndSmallZone(
            @PathVariable("game_code") Integer gameCode);

    /**
     * 获取游戏大小区
     */
    @GetMapping("/game/app/{game_code}/big_small_zone/list")
    public ResponsePacket<List<RedisGameBigZone>> getBigZoneAndSmallZoneForApp(
            @RequestHeader("Authorization") String token,
            @PathVariable("game_code") Integer gameCode);

    /**
     * 获取游戏副本和大小区
     */
    @GetMapping("/game/app/{game_code}/raid_server/list")
    public ResponsePacket<RaidAndGameServerVo> getRaidAndServer(
            @RequestHeader("Authorization") String token,
            @PathVariable("game_code") Integer gameCode);

    @GetMapping("/game/{game_code}/small_zone_code/{small_zone_code}/name")
    public ResponsePacket<String> getSmallZoneName(@PathVariable("game_code") Integer gameCode,
            @PathVariable("small_zone_code") Integer smallZoneCode);

    @GetMapping("/game/{game_code}/big_zone_code/{big_zone_code}/name")
    public ResponsePacket<String> getBigZoneName(@PathVariable("game_code") Integer gameCode,
            @PathVariable("big_zone_code") Integer bigZoneCode);

    @GetMapping("/game/{game_code}/across_zone_code/{across_zone_code}/name")
    public ResponsePacket<String> getAcrossZoneName(@PathVariable("game_code") Integer gameCode,
            @PathVariable("across_zone_code") Integer acrossZoneCode);
}

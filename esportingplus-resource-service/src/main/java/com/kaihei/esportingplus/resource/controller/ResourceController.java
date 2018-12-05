package com.kaihei.esportingplus.resource.controller;

import com.kaihei.esportingplus.api.feign.ResourceServiceClient;
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
import com.kaihei.esportingplus.resource.data.manager.ConfigBaojiService;
import com.kaihei.esportingplus.resource.data.manager.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resources")
@Api(tags = {"资源服务接口"})
public class ResourceController implements ResourceServiceClient {

    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ConfigBaojiService configBaojiService;

    @Override
    public ResponsePacket<ResourceVO> getResourceById(String id) {
        return null;
    }

    @ApiOperation(value = "根椐小区获得对应大区跨区")
    @Override
    public ResponsePacket<RedisSmallZoneRefAcrossZone> getAcrossZoneFromSmallZoneCode(
            @ApiParam(value = "游戏代码", required = true) @PathVariable("game_code") Integer gameCode,
            @ApiParam(value = "游戏小区代码", required = true) @PathVariable("small_zone_code") Integer smallZoneCode) {
        return ResponsePacket
                .onSuccess(resourceService.getAcrossZoneFromSmallZoneCode(gameCode, smallZoneCode));
    }

    @ApiOperation(value = "根椐跨区获得对应大区小区")
    @Override
    public ResponsePacket<List<RedisGameBigZone>> getBigOrSmallZoneByAcrossCode(
            @RequestHeader("Authorization") String token,
            @ApiParam(value = "游戏代码", required = true) @PathVariable("game_code") Integer gameCode,
            @ApiParam(value = "游戏跨区代码", required = true) @PathVariable("zone_across_code") Integer zoneAcrossCode) {
        return ResponsePacket
                .onSuccess(resourceService.getBigOrSmallZoneByAcrossCode(gameCode, zoneAcrossCode));
    }
    @ApiOperation(value = "根椐跨区获得对应所属小区")
    @Override
    public ResponsePacket<List<RedisGameSmallZone>> getSmallZoneByAcrossCode( @ApiParam(value = "游戏代码", required = true) @PathVariable("game_code")Integer gameCode,
            @ApiParam(value = "游戏跨区代码", required = true) @PathVariable("zone_across_code")Integer zoneAcrossCode) {
        return ResponsePacket
                .onSuccess(resourceService.getSmallZoneByAcrossCode(gameCode, zoneAcrossCode));
    }

    @ApiOperation(value = "根椐大区code获得对应小区")
    @Override
    public ResponsePacket<List<RedisGameSmallZone>> getSmallZoneByBigCode(
            @ApiParam(value = "游戏代码", required = true) @PathVariable("game_code") Integer gameCode,
            @ApiParam(value = "游戏大区代码", required = true) @PathVariable("zone_big_code") Integer zoneBigCode) {
        return ResponsePacket
                .onSuccess(resourceService.getSmallZoneByBigCode(gameCode, zoneBigCode));
    }

    @ApiOperation(value = "根椐大区code获得对应大区和小区")
    @Override
    public ResponsePacket<RedisGameBigZone> getBigAndSmallZoneByBigCode(
            @ApiParam(value = "游戏代码", required = true) @PathVariable("game_code") Integer gameCode,
            @ApiParam(value = "游戏大区代码", required = true) @PathVariable("zone_big_code") Integer zoneBigCode) {
        return ResponsePacket
                .onSuccess(resourceService.getBigAndSmallZoneByBigCode(gameCode, zoneBigCode));
    }

    @ApiOperation(value = "获取游戏职业列表")
    @Override
    public ResponsePacket<List<RedisTopCareer>> getCareerByGameCode(
            @ApiParam(value = "游戏代码", required = true) @PathVariable("game_code") Integer gameCode) {
        return ResponsePacket.onSuccess(resourceService.getCareerByGameCode(gameCode));
    }

    @ApiOperation(value = "App调用-获取游戏职业列表")
    @Override
    public ResponsePacket<List<FrontTopCareer>> getCareerByGameCodeForApp(
            @RequestHeader("Authorization") String token,
            @ApiParam(value = "游戏代码", required = true) @PathVariable("game_code") Integer gameCode) {
        return ResponsePacket.onSuccess(resourceService.getFrontTopCareer(gameCode));
    }

    @ApiOperation(value = "获取游戏某个职业列表")
    @Override
    public ResponsePacket<RedisTopCareer> getCareerByGameCodeAndTopCareerCode(
            @ApiParam(value = "游戏代码", required = true) @PathVariable("game_code") Integer gameCode,
            @ApiParam(value = "职业代码", required = true) @PathVariable("career_code") Integer careerCode) {
        return ResponsePacket.onSuccess(
                resourceService.getCareerByGameCodeAndTopCareerCode(gameCode, careerCode));
    }

    /**
     * 获取游戏列表
     */
    @ApiOperation(value = "获取游戏列表")
    @Override
    public ResponsePacket<List<RedisGame>> getGameList() {
        return ResponsePacket.onSuccess(resourceService.getGameList());
    }

    @ApiOperation(value = "App调用-获取游戏列表")
    @Override
    public ResponsePacket<List<RedisGame>> getGameListForApp(
            @RequestHeader("Authorization") String token) {
        return ResponsePacket.onSuccess(resourceService.getGameList());
    }

    /**
     * 获取游戏副本列表
     */
    @ApiOperation(value = "获取游戏副本列表")
    @Override
    public ResponsePacket<List<RedisGameRaid>> getGameRaids(
            @ApiParam(value = "游戏代码", required = true) @PathVariable("game_code") Integer gameCode) {
        return ResponsePacket.onSuccess(resourceService.getGameRaids(gameCode));
    }

    @ApiOperation(value = "APP调用-获取游戏副本列表")
    @Override
    public ResponsePacket<List<SimpleGameRaid>> getGameRaidsApp(
            @RequestHeader("Authorization") String token,
            @ApiParam(value = "游戏代码", required = true) @PathVariable("game_code") Integer gameCode) {
        return ResponsePacket.onSuccess(resourceService.getGameRaidsForApp(gameCode));
    }

    /**
     * 获取单个游戏副本
     */
    @ApiOperation(value = "获取单个游戏副本")
    @Override
    public ResponsePacket<RedisGameRaid> getSingleGameRaid(
            @ApiParam(value = "游戏代码", required = true) @PathVariable("game_code") Integer gameCode,
            @ApiParam(value = "副本代码", required = true) @PathVariable("raid_code") Integer raidCode) {
        return ResponsePacket.onSuccess(resourceService.getSingleGameRaid(gameCode, raidCode));
    }

    @ApiOperation(value = "获取认证副本列表")
    @Override
    public ResponsePacket<List<BaseGameRaidVo>> getCertGameRaids(
            @ApiParam(value = "游戏代码", required = true) @PathVariable("game_code") Integer gameCode) {
        return ResponsePacket.onSuccess(resourceService.getCertGameRaids(gameCode));
    }

    @ApiOperation(value = "通过认证副本code查询认证副本信息")
    @Override
    public ResponsePacket<BaseGameRaidVo> getCertSingleGameRaid(
            @ApiParam(value = "游戏代码", required = true) @PathVariable("game_code") Integer gameCode,
            @ApiParam(value = "认证副本代码", required = true) @PathVariable("cert_raid_code") Integer certRaidCode) {
        return ResponsePacket
                .onSuccess(resourceService.getCertSingleGameRaid(gameCode, certRaidCode));
    }

    @ApiOperation(value = "通过认证副本code找到下面所属副本")
    @Override
    public ResponsePacket<List<RedisGameRaid>> getGameRaidThroughCertCode(
            @ApiParam(value = "游戏代码", required = true) @PathVariable("game_code") Integer gameCode,
            @ApiParam(value = "认证副本代码", required = true) @PathVariable("cert_raid_code") Integer certRaidCode) {
        return ResponsePacket
                .onSuccess(resourceService.getGameRaidThroughCertCode(gameCode, certRaidCode));
    }

    /**
     * 获取跨区列表
     */
    @ApiOperation(value = "获取跨区列表")
    @Override
    public ResponsePacket<List<RedisGameAcrossZone>> getGameAcrossZone(
            @ApiParam(value = "游戏代码", required = true) @PathVariable("game_code") Integer gameCode) {
        return ResponsePacket.onSuccess(resourceService.getGameAcrossZone(gameCode));
    }

    @ApiOperation(value = "App调用-获取跨区列表")
    @Override
    public ResponsePacket<List<RedisGameAcrossZone>> getGameAcrossZoneForApp(
            @RequestHeader("Authorization") String token,
            @ApiParam(value = "游戏代码", required = true) @PathVariable("game_code") Integer gameCode) {
        return ResponsePacket.onSuccess(resourceService.getGameAcrossZone(gameCode));
    }

    @ApiOperation(value = "获取暴鸡等级对应的暴鸡收益系数")
    @Override
    public ResponsePacket<BigDecimal> getBaojiLevelRate(
            @ApiParam(value = "暴鸡等级", required = true) @PathVariable("baoji_level") Integer baojiLevel) {
        return ResponsePacket.onSuccess(configBaojiService.getBaojiLevelRate(baojiLevel));
    }

    @ApiOperation(value = "批量获取暴鸡等级对应的暴鸡收益系数")
    @Override
    public ResponsePacket<List<BaojiLevelRateVo>> getBaojiLevelRateBatch(
            @ApiParam(value = "暴鸡等级系数", required = true)
            @RequestBody List<Integer> params) {
        return ResponsePacket.onSuccess(configBaojiService.getBaojiLevelRateBatch(params));
    }

    @ApiOperation(value = "获取游戏大小区")
    @Override
    public ResponsePacket<List<RedisGameBigZone>> getBigZoneAndSmallZone(
            @ApiParam(value = "游戏代码", required = true) @PathVariable("game_code") Integer gameCode) {
        return ResponsePacket.onSuccess(resourceService.getAllBigZoneAndSmallZone(gameCode));
    }

    @ApiOperation(value = "App调用-获取游戏大小区")
    @Override
    public ResponsePacket<List<RedisGameBigZone>> getBigZoneAndSmallZoneForApp(
            @RequestHeader("Authorization") String token,
            @ApiParam(value = "游戏代码", required = true) @PathVariable("game_code") Integer gameCode) {
        return ResponsePacket.onSuccess(resourceService.getAllBigZoneAndSmallZone(gameCode));
    }

    @ApiOperation(value = "App调用-获取游戏副本和大小区")
    @Override
    public ResponsePacket<RaidAndGameServerVo> getRaidAndServer(
            @RequestHeader("Authorization") String token,
            @ApiParam(value = "游戏代码", required = true) @PathVariable("game_code") Integer gameCode) {
        return ResponsePacket.onSuccess(resourceService.getRaidAndServer(gameCode));
    }

    @ApiOperation(value = "获取小区名称")
    @Override
    public ResponsePacket<String> getSmallZoneName(
            @ApiParam(value = "游戏代码", required = true) @PathVariable("game_code") Integer gameCode,
            @ApiParam(value = "小区code", required = true) @PathVariable("small_zone_code") Integer smallZoneCode) {
        return ResponsePacket.onSuccess(resourceService.getSmallZoneName(gameCode, smallZoneCode));
    }

    @ApiOperation(value = "获取大区名称")
    @Override
    public ResponsePacket<String> getBigZoneName(
            @ApiParam(value = "游戏代码", required = true) @PathVariable("game_code") Integer gameCode,
            @ApiParam(value = "大区code", required = true) @PathVariable("big_zone_code") Integer bigZoneCode) {
        return ResponsePacket.onSuccess(resourceService.getBigZoneName(gameCode, bigZoneCode));
    }

    @ApiOperation(value = "获取跨区名称")
    @Override
    public ResponsePacket<String> getAcrossZoneName(
            @ApiParam(value = "游戏代码", required = true) @PathVariable("game_code") Integer gameCode,
            @ApiParam(value = "跨区code", required = true) @PathVariable("across_zone_code") Integer acrossZoneCode) {
        return ResponsePacket
                .onSuccess(resourceService.getAcrossZoneName(gameCode, acrossZoneCode));
    }
}

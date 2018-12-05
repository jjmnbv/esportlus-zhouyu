package com.kaihei.esportingplus.resource.data.manager;

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
import com.kaihei.esportingplus.api.vo.SimpleGameRaid;
import java.util.List;

/**
 * @author zhangfang
 */
public interface ResourceService {

    /**
     * 从小区获取对应跨区
     */
    public RedisSmallZoneRefAcrossZone getAcrossZoneFromSmallZoneCode(Integer gameCode,
            Integer smallZoneCode);

    /**
     * 从跨区获取对应的大区小区
     */
    List<RedisGameBigZone> getBigOrSmallZoneByAcrossCode(Integer gameCode, Integer zoneAcrossCode);


    /**
     * 通关大区找到小区列表
     */
    public List<RedisGameSmallZone> getSmallZoneByBigCode(Integer gameCode, Integer zoneBigCode);

    /**
     * 获取游戏职业列表
     */
    List<RedisTopCareer> getCareerByGameCode(Integer gameCode);

    /**
     * 获取游戏某个顶级下的职业
     */
    public RedisTopCareer getCareerByGameCodeAndTopCareerCode(Integer gameCode, Integer careerCode);

    /**
     * 获取游戏职业，经过封装，给前端调用
     * @param gameCode
     * @return
     */
    public List<FrontTopCareer> getFrontTopCareer(Integer gameCode);
    /**
     * 获取游戏列表
     */
    public List<RedisGame> getGameList();

    /**
     * 获取游戏副本
     */
    public List<RedisGameRaid> getGameRaids(Integer gameCode);

    /**
     * 根据游戏code和副本code获取单个副本
     */
    public RedisGameRaid getSingleGameRaid(Integer gameCode, Integer raidCode);

    /**
     * 获取认证副本列表
     * @param gameCode
     * @return
     */
    public List<BaseGameRaidVo> getCertGameRaids(Integer gameCode);
    /**
     * 根据游戏code和认证副本code获取认证副本信息
     * @param gameCode
     * @return
     */
    public BaseGameRaidVo getCertSingleGameRaid(Integer gameCode, Integer certRaidCode);

    /**
     * 通过游戏code和认证副本找到下面所属副本
     * @param gameCode
     * @param certRaidCode
     * @return
     */
    public List<RedisGameRaid> getGameRaidThroughCertCode(Integer gameCode, Integer certRaidCode);

    /**
     * 获取游戏跨区列表
     */
    public List<RedisGameAcrossZone> getGameAcrossZone(Integer gameCode);

    /**
     *  通过跨区找到小区列表
     * @param gameCode
     * @param zoneAcrossCode
     * @return
     */
    public List<RedisGameSmallZone> getSmallZoneByAcrossCode(Integer gameCode,Integer zoneAcrossCode);

    /**
     * 获取游戏所有的大小区
     */
    public List<RedisGameBigZone> getAllBigZoneAndSmallZone(Integer gameCode);

    /**
     * 获取游戏大区code获得游戏大区及其小区
     * @param gameCode
     * @param zoneBigCode
     * @return
     */
    public RedisGameBigZone getBigAndSmallZoneByBigCode(Integer gameCode, Integer zoneBigCode);
    /**
     * 获得游戏服务器和大小区
     */
    public RaidAndGameServerVo getRaidAndServer(Integer gameCode);

    /**
     * 获取小区服务器名称
     */
    public String getSmallZoneName(Integer gameCode, Integer smallZoneCode);

    /**
     * 获取大区服务器名称
     */
    public String getBigZoneName(Integer gameCode, Integer bigZoneCode);

    /**
     * 获取跨区服务器名称
     */
    public String getAcrossZoneName(Integer gameCode, Integer acrossZoneCode);

    List<SimpleGameRaid> getGameRaidsForApp(Integer gameCode);
}

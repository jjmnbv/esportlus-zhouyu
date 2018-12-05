package com.kaihei.esportingplus.gamingteam.data.manager;

import com.kaihei.esportingplus.api.vo.RedisGameRaid;
import com.kaihei.esportingplus.api.vo.RedisSmallZoneRefAcrossZone;
import java.util.List;

/**
 * 调用资源服务查询游戏配置
 * 在容器启动时调用并放入 redis 中
 * @author liangyi
 */
public interface GameConfigService {

    /**
     * 查询游戏副本配置列表
     * 第一次调用先调接口查, 查询到后放入 redis 中
     * @param gameCode
     * @return
     */
    List<RedisGameRaid> getGameRaidList(Integer gameCode);

    /**
     * 查询游戏副本配置
     * @param gameCode
     * @param raidCode
     * @return
     */
    RedisGameRaid getGameRaid(Integer gameCode, Integer raidCode);

    /**
     * 通过小区查找对应的大区跨区信息
     * @param gameCode
     * @param smallZoneCode
     * @return
     */
    RedisSmallZoneRefAcrossZone getBigAndAcrossZoneBySmallZoneCode(Integer gameCode,Integer smallZoneCode);

}

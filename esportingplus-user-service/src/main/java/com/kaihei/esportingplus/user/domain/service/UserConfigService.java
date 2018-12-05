package com.kaihei.esportingplus.user.domain.service;

import com.kaihei.esportingplus.api.vo.RedisGameRaid;
import com.kaihei.esportingplus.api.vo.RedisSmallZoneRefAcrossZone;
import com.kaihei.esportingplus.user.api.vo.UserGameDetailRoleInfoVo;
import java.util.List;
import java.util.Map;

/**
 *  缓存到本地Redis的数据服务
 * @author zhangfang
 */
public interface UserConfigService {
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
    RedisSmallZoneRefAcrossZone getBigAndAcrossZoneBySmallCode(Integer gameCode,Integer smallZoneCode);

    /**
     * 通过认证的副本code找到可加入的副本
     * @param gameCode
     * @param certRaidCode
     * @return
     */
    public List<RedisGameRaid> getGameRaidThroughCertCode(Integer gameCode,Integer certRaidCode);

    /**
     * 获取小区名称
     * @param gameCode
     * @param smallZoneCode
     * @return
     */
    public String getSmallZoneName(Integer gameCode,Integer smallZoneCode);

    /**
     * 获取一个用户某个角色的认证信息
     * @param userRoleKey
     * @param roleId
     * @return
     */
    public UserGameDetailRoleInfoVo getUserRoleDetailRoleInfo(String userRoleKey,String roleId);
    /**
     * 获取一个用户角色的认证集合
     * @param userRoleKey
     * @return
     */
    public Map<String, UserGameDetailRoleInfoVo> getUserAllRoleDetailRoleInfo(String userRoleKey);

    /**
     * 通过跨区code找到对应的小区code
     * @param gameCode
     * @param zoneAcrossCode
     * @return
     */
    List<Integer> findSmallCodeFromAcrossCode(Integer gameCode, Integer zoneAcrossCode);
}

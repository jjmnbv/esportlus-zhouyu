package com.kaihei.esportingplus.resource.data.manager;

import com.kaihei.esportingplus.api.vo.BaojiDanRangeVO;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamTypeDetailVO;

/**
 * @author liangyi
 * 免费车队数据缓存
 */
public interface FreeTeamCacheManager {

    /**
     * 新增或更新 redis 中的免费车队类型
     * @param freeTeamTypeDetailVO
     */
    void addOrUpdateFreeTeamType(FreeTeamTypeDetailVO freeTeamTypeDetailVO);

    /**
     * 从 redis 中删除免费车队类型
     * @param freeTeamTypeId
     */
    void deleteFreeTeamType(Integer freeTeamTypeId);

    /**
     * 新增或更新 redis 中的暴鸡接单范围
     * @param baojiDanRangeVO
     */
    void addOrUpdateBaojiDanRange(BaojiDanRangeVO baojiDanRangeVO);

    /**
     * 删除 redis 中的暴鸡接单范围
     * @param gameId 游戏 id
     * @param baojiLevel {@link com.kaihei.esportingplus.common.enums.BaojiLevelEnum}
     */
    void deleteBaojiDanRange(Integer gameId, Integer baojiLevel);

    /**
     * 获取 redis 中的暴鸡接单范围
     * @param gameId 游戏 id
     * @param baojiLevel {@link com.kaihei.esportingplus.common.enums.BaojiLevelEnum}
     */
    BaojiDanRangeVO getBaojiDanRange(Integer gameId, Integer baojiLevel);
}

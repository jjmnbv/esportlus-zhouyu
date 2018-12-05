package com.kaihei.esportingplus.resource.domain.service;

import com.kaihei.esportingplus.api.params.BaojiDanRangeBatchParams;
import com.kaihei.esportingplus.api.vo.BaojiDanRangeVO;
import java.util.List;

/**
 * @author liangyi
 */
public interface BaojiDanRangeService {

    /**
     * 根据游戏 id 和暴鸡等级 code 查询暴鸡接单范围
     * @param gameId
     * @param baojiLevel {@link com.kaihei.esportingplus.common.enums.BaojiLevelEnum}
     * @return
     */
    BaojiDanRangeVO getBaojiDanRangeByGameAndLevel(Integer gameId, Integer baojiLevel);

    /**
     * 根据游戏 id 查询所有暴鸡接单范围
     * @param gameId
     * @return
     */
    List<BaojiDanRangeVO> getAllBaojiDanRangeByGameId(Integer gameId);

    /**
     * 新增暴鸡接单范围, 按游戏添加
     * @param baojiDanRangeBatchParams
     */
    void addBaojiDanRange(BaojiDanRangeBatchParams baojiDanRangeBatchParams);

    /**
     * 修改暴鸡接单范围, 按游戏修改
     * @param baojiDanRangeBatchParams
     */
    void updateBaojiDanRange(BaojiDanRangeBatchParams baojiDanRangeBatchParams);

    /**
     * 删除暴鸡接单范围
     * @param baojiDanRangeIdList
     */
    void deleteBaojiDanRangeBatch(List<Integer> baojiDanRangeIdList);

    /**
     * 初始化暴鸡接单范围到 redis
     */
    void initBaojiDanRangeCache();
}

package com.kaihei.esportingplus.resource.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.resource.domain.entity.BaojiDanRange;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author liangyi
 */
public interface BaojiDanRangeRepository extends CommonRepository<BaojiDanRange> {

    /**
     * 根据游戏 id 查询暴鸡接单范围
     * @param gameId
     * @return
     */
    List<BaojiDanRange> selectBaojiDanRangeList(@Param("gameId") Integer gameId);

    /**
     * 根据游戏 id 和暴鸡等级 id 查询暴鸡接单范围
     * @param gameId
     * @param baojiLevel {@link com.kaihei.esportingplus.common.enums.BaojiLevelEnum}
     * @return
     */
    BaojiDanRange selectByGameAndBaojiLevel(
            @Param("gameId") Integer gameId, @Param("baojiLevel") Integer baojiLevel);
}
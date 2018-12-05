package com.kaihei.esportingplus.resource.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.resource.domain.entity.PVPBaojiPricingConfig;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author liangyi
 */
public interface PVPBaojiPricingConfigRepository
        extends CommonRepository<PVPBaojiPricingConfig> {

    /**
     * 批量查询暴鸡段位计价配置
     * @param gameId 游戏 id
     * @param bossGameDanIdList 老板段位 id
     * @param baojiLevelCodeList 暴鸡等级 code
     * @param pricingType 计价类型
     * @param status 状态
     * @return
     */
    List<PVPBaojiPricingConfig> selectBaojiGameDanIncome(
            @Param("gameId") Integer gameId,
            @Param("bossGameDanIdList") List<Integer> bossGameDanIdList,
            @Param("baojiLevelCodeList") List<Integer> baojiLevelCodeList,
            @Param("pricingType") Integer pricingType,
            @Param("status") Integer status);
}
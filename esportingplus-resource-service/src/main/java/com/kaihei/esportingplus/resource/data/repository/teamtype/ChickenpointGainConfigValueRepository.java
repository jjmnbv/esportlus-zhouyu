package com.kaihei.esportingplus.resource.data.repository.teamtype;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.resource.domain.entity.freeteam.ChickenpointGainConfigValue;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Param;

public interface ChickenpointGainConfigValueRepository extends CommonRepository<ChickenpointGainConfigValue> {

    /**
     * 批量更新
     */
    Integer batchUpdateValues(List<ChickenpointGainConfigValue> configValues);

    /**
     * 批量插入
     */
    Integer batchInsertConfigValues(List<ChickenpointGainConfigValue> chickenpointGainConfigValues);

    /**
     * 车队类型Id + 暴鸡身份 + 段位集合 ---> 打了 或 赢配置
     */
    List<ChickenpointGainConfigValue> selectPisitiveConfigValues(
            @Param("freeTeamTypeId") Integer freeTeamTypeId,
            @Param("baojiIdentityId") Integer baojiIdentityId,
            @Param("gameDanIds") Set<Integer> gameDanIds,
            @Param("positiveGameResultId") Integer positiveGameResultId
    );

    /**
     * 根据id集合删除配置
     */
    void deleteByIds(@Param("ids") List<Integer> ids);
}
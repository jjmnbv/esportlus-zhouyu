package com.kaihei.esportingplus.resource.data.repository.teamtype;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.resource.domain.entity.freeteam.TeamSettlementMode;
import java.util.List;

/**
 * @author liangyi
 */
public interface TeamSettlementModeRepository extends CommonRepository<TeamSettlementMode> {

    /**
     * 根据车队类型 id 查询其结算方式
     * @param teamTypeId 车队类型 id
     * @return
     */
    List<TeamSettlementMode> selectByTeamTypeId(Integer teamTypeId);


    /**
     * 根据车队类型 id 删除其结算方式
     * @param teamTypeId 车队类型 id
     */
    void deleteByTeamTypeId(Integer teamTypeId);
}
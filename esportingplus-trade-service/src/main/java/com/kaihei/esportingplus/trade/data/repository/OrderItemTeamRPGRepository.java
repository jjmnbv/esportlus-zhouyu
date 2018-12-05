package com.kaihei.esportingplus.trade.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.trade.api.params.StudioOrderQueryParams;
import com.kaihei.esportingplus.trade.api.vo.StudioOrderListVo;
import com.kaihei.esportingplus.trade.domain.entity.OrderItemTeamRPG;
import com.kaihei.esportingplus.trade.domain.entity.TeamOrderCount;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderItemTeamRPGRepository extends CommonRepository<OrderItemTeamRPG> {

    void insertOrderItemTeam(OrderItemTeamRPG orderItemTeamRPG);

    OrderItemTeamRPG selectOrderItemTeamByOrderSequeue(String sequeue);

    OrderItemTeamRPG getByOrderId(String orderId);

    List<StudioOrderListVo> selectStudioOrderVoList(StudioOrderQueryParams params);

    Integer updateTeamStatusById(@Param("teamStatus") Integer teamStatus,@Param("id") Long id);

    List<TeamOrderCount> countTeamComplitedBossOrderStatisticsByTeamSequences(
            @Param("teamSequences") List<String> teamSequences);
}
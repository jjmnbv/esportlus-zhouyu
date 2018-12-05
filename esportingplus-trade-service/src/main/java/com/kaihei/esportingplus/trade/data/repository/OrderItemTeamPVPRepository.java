package com.kaihei.esportingplus.trade.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.trade.domain.entity.OrderItemTeamPVP;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderItemTeamPVPRepository extends CommonRepository<OrderItemTeamPVP> {

    void updateTeamStatusById(@Param("teamStatus") Integer teamStatus,@Param("id") Long id);

    OrderItemTeamPVP getByOrderId(String orderId);
}
package com.kaihei.esportingplus.trade.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.trade.domain.entity.OrderItemTeamPVPFree;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderItemTeamPVPFreeRepository extends CommonRepository<OrderItemTeamPVPFree> {

    List<OrderItemTeamPVPFree> getByTeamSequenceIdAndUids(@Param("teamSequeue") String teamSequeue,@Param("uids") List<String> uids);

    int updateBatch(@Param("items") List<OrderItemTeamPVPFree> items);

    int update(@Param("item") OrderItemTeamPVPFree items);

    OrderItemTeamPVPFree getByOrderId(String orderId);

    List<OrderItemTeamPVPFree> getByTeamSequence(@Param("teamSequeue") String teamSequeue);
}
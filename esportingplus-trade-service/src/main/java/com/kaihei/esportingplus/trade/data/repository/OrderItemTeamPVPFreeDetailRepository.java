package com.kaihei.esportingplus.trade.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.trade.domain.entity.OrderItemTeamPVPFreeDetail;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderItemTeamPVPFreeDetailRepository extends CommonRepository<OrderItemTeamPVPFreeDetail> {

    int insertBatch(@Param("items") List<OrderItemTeamPVPFreeDetail> items);

    List<OrderItemTeamPVPFreeDetail> findBaojiIncomeDetails(@Param("baojiUid")String baojiUid,@Param("teamSequence")String teamSequence,@Param("bossUids") List<String> bossUids);

}
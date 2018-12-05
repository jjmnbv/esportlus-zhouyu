package com.kaihei.esportingplus.trade.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.trade.api.params.CheckTeamMemberPayedParams;
import com.kaihei.esportingplus.trade.api.params.PVPFreeOrdersForBackGroundParams;
import com.kaihei.esportingplus.trade.api.params.StudioUserOrderStatisticsQueryParams;
import com.kaihei.esportingplus.trade.api.vo.BaojiOrderVO;
import com.kaihei.esportingplus.trade.api.vo.BossOrderVO;
import com.kaihei.esportingplus.trade.api.vo.PVPBaojiOrderVO;
import com.kaihei.esportingplus.trade.api.vo.PVPBossOrderVO;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeBossOrderForBackGroundVO;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeOrderIncomeForBackGroundVO;
import com.kaihei.esportingplus.trade.api.vo.StudioOrderVo;
import com.kaihei.esportingplus.trade.api.vo.TeamBossOrderVO;
import com.kaihei.esportingplus.trade.domain.entity.Order;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderRepository extends CommonRepository<Order> {

    Order getBySequenceId(@Param("sequenceId") String id);
    Order getPvpOrderBySequenceId(@Param("sequenceId") String id);
    Order getPvpFreeOrderBySequenceId(@Param("sequenceId") String id);
    List<Order> getByTeamSequenceIdAndUids(@Param("teamSequeue") String teamSequeue,@Param("uids") List<String> uids);

    void updateSelectiveBySequenceId(@Param("order") Order order);

    Order getByOuterTradeNo(@Param("outerTradeNo") String outerTradeNo);

    void insertOrder(Order order);

    List<Order> getByUidAndTeamSequeue(@Param("uid") String uid,@Param("teamSequeue") String teamSequeue);

    List<Order> getByTeamSequeue(@Param("teamSequeue") String teamSequeue);

    List<Order> getLeaveTeamOrders(@Param("teamSequeue") String teamSequeue);

    List<Order> getByCondiction(@Param("params") CheckTeamMemberPayedParams params);

    List<Order> selectTeamOtherOrders(@Param("teamSequeue")String teamSequeue, @Param("sequence")String sequeue);

    Order selectLastUserOrder(@Param("uid") String uid,@Param("teamSequeue")String teamSequeue);
    Order selectLastPaidPVPUserOrder(@Param("uid") String uid,@Param("teamSequeue")String teamSequeue);
    Order selectPVPFreeUserOrder(@Param("uid") String uid,@Param("teamSequeue")String teamSequeue);
    List<BossOrderVO> selectBossOrderVoList(@Param("userId") String userId);

    List<BaojiOrderVO>  selectBaojiOrderVoList(@Param("userId") String userId);
    List<PVPBossOrderVO> selectPvpBossOrderVoList(@Param("userId") String userId);

    List<PVPBaojiOrderVO>  selectPvpBaojiOrderVoList(@Param("userId") String userId);
    List<StudioOrderVo> selectBaojiOrderVoListByUidsAndCreateTime(
            StudioUserOrderStatisticsQueryParams studioUserOrderStatisticsQueryParams);

    TeamBossOrderVO selectRPGBossOrderByTeamSequenceAndUid(@Param("teamSequence") String teamSequence,
            @Param("userId") String userId);

    Order selectPVPBossOrderByTeamSequenceAndUid(@Param("teamSequence") String teamSequence,
            @Param("userId") String userId);

    List<Order> selectPVPTeamOtherOrders(@Param("teamSequeue")String teamSequeue, @Param("sequence")String sequeue);
    List<Order> selectPVPFreeTeamOtherOrders(@Param("teamSequeue")String teamSequeue, @Param("sequence")String sequeue);

    List<Order> selectPVPFreeBossOrders(@Param("userId") String userId);

    List<Order> selectPVPFreeBaojiOrders(@Param("userId") String userId);

    List<PVPFreeOrderIncomeForBackGroundVO> selectPVPFreeBossGiveBaojiIncome(@Param("sequence") String sequence);
    List<PVPFreeOrderIncomeForBackGroundVO> selectPVPFreeBaojiFromBossIncome(@Param("sequence") String sequence);

    List<PVPFreeBossOrderForBackGroundVO> selectPVPFreeBossOrderListForBackGround(@Param("params") PVPFreeOrdersForBackGroundParams params);
    List<Order> selectPVPFreeBaojiOrderListForBackGround(@Param("params") PVPFreeOrdersForBackGroundParams params);
}
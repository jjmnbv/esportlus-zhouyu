package com.kaihei.esportingplus.user.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.user.api.vo.UserOrderCountVo;
import com.kaihei.esportingplus.user.domain.entity.MembersOrderCount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MembersOrderCountRepository extends CommonRepository<MembersOrderCount> {

    int updateTodayData(@Param("uid") String uid, @Param("todayPlace") int todayPlace,
                        @Param("todayAccept") int todayAccept, @Param("orderType") int orderType);

    MembersOrderCount selectUserDateByUid(@Param("uid") String uid,@Param("orderType") int orderType);

    int updatePlaceData(@Param("uid") String uid,@Param("orderType") int orderType);

    int updateOrderData(@Param("uid") String uid,@Param("orderType") int orderType);

    UserOrderCountVo selectSumByUid(String uid);

    List<MembersOrderCount> selectOverTimeTodayData(String uid);
}
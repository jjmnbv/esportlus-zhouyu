package com.kaihei.esportingplus.trade.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.trade.domain.entity.OrderCoupon;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderCouponRepository extends CommonRepository<OrderCoupon> {
    int insertOrderCoupon(List<OrderCoupon> orderConponList);

    OrderCoupon selectByOrderCouponByOrderId(@Param("orderId") Long orderId);
}
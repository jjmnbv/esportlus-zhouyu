package com.kaihei.esportingplus.trade.domain.service;

import com.kaihei.esportingplus.trade.domain.entity.OrderCoupon;
import java.util.List;

/**
 *@Description: 优惠券接口
 *
 *@author  Orochi-Yzh
 *@dateTime  2018/11/6 17:15
*/
public interface CouponService {

   void consumeCouoonParams(String userId, Long orderId, String sequeue);

   int insertOrderCoupon(List<OrderCoupon> orderConponList);
}

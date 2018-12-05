package com.kaihei.esportingplus.payment.data.jpa.repository;

import com.kaihei.esportingplus.payment.domain.entity.GCoinRefundOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 暴鸡币退款订单表
 *
 * @author xusisi
 * @create 2018-09-25 15:07
 **/
public interface GCoinRefundRepository extends JpaRepository<GCoinRefundOrder, Long>, JpaSpecificationExecutor<GCoinRefundOrder> {

    GCoinRefundOrder findOneByOrderId(String orderId);

    GCoinRefundOrder findOneByOrderIdAndUserId(String orderId, String userId);

    GCoinRefundOrder findOneByOrderTypeAndOutRefundNo(String orderType, String outRefundNo);

}

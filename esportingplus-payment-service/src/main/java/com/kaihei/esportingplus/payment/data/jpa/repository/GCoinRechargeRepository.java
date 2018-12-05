package com.kaihei.esportingplus.payment.data.jpa.repository;

import com.kaihei.esportingplus.payment.domain.entity.GCoinRechargeOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 暴鸡币充值订单表
 *
 * @author xusisi
 * @create 2018-09-25 15:07
 **/
public interface GCoinRechargeRepository extends JpaRepository<GCoinRechargeOrder, Long>, JpaSpecificationExecutor<GCoinRechargeOrder> {

    GCoinRechargeOrder findOneByOrderId(String orderId);

    GCoinRechargeOrder findOneByOrderIdAndUserId(String orderId, String userId);

    GCoinRechargeOrder findOneByPaymentOrderNoAndUserId(String paymentOrderNo, String userId);

    GCoinRechargeOrder findOneByPaymentOrderNoAndRechargeTypeAndOrderType(String paymentOrderNo, String rechargeType, String orderType);

}

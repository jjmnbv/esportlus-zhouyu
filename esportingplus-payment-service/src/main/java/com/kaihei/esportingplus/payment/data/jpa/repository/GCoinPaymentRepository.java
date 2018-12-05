package com.kaihei.esportingplus.payment.data.jpa.repository;

import com.kaihei.esportingplus.payment.domain.entity.GCoinPaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 暴鸡币支付订单表
 *
 * @author xusisi
 * @create 2018-09-23 15:53
 **/
public interface GCoinPaymentRepository extends JpaRepository<GCoinPaymentOrder, Long>, JpaSpecificationExecutor<GCoinPaymentOrder> {


    public GCoinPaymentOrder findOneByOrderIdAndUserId(String orderId, String userId);

    public GCoinPaymentOrder findOneByOrderId(String orderId);

    public GCoinPaymentOrder findOneByOrderTypeAndOutTradeNo(String orderType, String outTradeNo);

}

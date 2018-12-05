package com.kaihei.esportingplus.payment.data.jpa.repository;

import com.kaihei.esportingplus.payment.domain.entity.WithdrawOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 提现订单Repository
 *
 * @author xusisi
 * @create 2018-08-18 10:21
 **/
public interface WithdrawOrderRepository extends JpaRepository<WithdrawOrder, Long>, JpaSpecificationExecutor<WithdrawOrder> {

    WithdrawOrder findOneByOrderIdAndUserId(String orderId, String userId);

    /**
     * 根据第三方提现订单号获取订单信息
     * @param outTradeNo
     * @return
     */
    WithdrawOrder findOneByOutTradeNo(String outTradeNo);

}

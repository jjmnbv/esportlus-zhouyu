package com.kaihei.esportingplus.payment.data.jpa.repository;

import com.kaihei.esportingplus.payment.domain.entity.DeductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @program: esportingplus
 * @description: 后台扣款订单
 * @author: xusisi
 * @create: 2018-10-11 14:29
 **/
public interface DeductOrderRepository extends JpaRepository<DeductOrder, Long>, JpaSpecificationExecutor<DeductOrder> {

    public DeductOrder findOneByCurrencyTypeAndOutTradeNoAndOrderType(String currencyType, String outTradeNo, String orderType);
}

package com.kaihei.esportingplus.payment.data.jpa.repository;

import com.kaihei.esportingplus.payment.domain.entity.ExternalRefundOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @program: esportingplus
 * @description: 退款相关repository
 * @author: xusisi
 * @create: 2018-10-31 15:51
 **/
public interface ExternalRefundOrderRepository extends JpaRepository<ExternalRefundOrder, Long>,
        JpaSpecificationExecutor<ExternalRefundOrder> {

    ExternalRefundOrder findOneByOrderId(String orderId);

    ExternalRefundOrder findOneByOutRefundNo(String outRefundNo);

    ExternalRefundOrder findByOutRefundNoAndOrderType(String outRefundNo, String orderType);

    ExternalRefundOrder findOneByTransactionId(String transactionId);

    //查询退款总金额
    @Query("select sum (refund.totalFee) from ExternalRefundOrder refund where refund.outTradeNo= ?1 and refund.orderType= ?2")
    Integer findTotalRefundByOrderTypeAndOutTradeNo(String outTadeNo, String orderType);

}

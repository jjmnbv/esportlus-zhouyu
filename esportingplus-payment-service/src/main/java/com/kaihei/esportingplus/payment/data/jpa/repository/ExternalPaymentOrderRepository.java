package com.kaihei.esportingplus.payment.data.jpa.repository;

import com.kaihei.esportingplus.payment.domain.entity.ExternalPaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * @program: esportingplus
 * @description: 后台支付订单DAO
 * @author: xusisi
 * @create: 2018-10-11 14:29
 **/
public interface ExternalPaymentOrderRepository extends JpaRepository<ExternalPaymentOrder, Long>, JpaSpecificationExecutor<ExternalPaymentOrder> {

    ExternalPaymentOrder findOneByOrderTypeAndOutTradeNo(String orderType, String outTradeNo);

    ExternalPaymentOrder findOneByOrderId(String orderId);

    @Query("select order.totalFee from ExternalPaymentOrder order where order.orderType=?1 and order.outTradeNo=?2")
    Integer findTotalFeeByOrderTypeAndOutTradeNo(String orderType, String outTradeNo);

    @Modifying
    @Query("update ExternalPaymentOrder exorder set exorder.state = ?1 ,exorder.paiedTime= ?2,exorder.transactionId= ?3 where exorder.orderType = " +
            "?4 and exorder.outTradeNo= ?5")
    void payNotifyUpdate(String state, Date time, String tansactionId, String orderType, String outTradeNo);

}

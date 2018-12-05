package com.kaihei.esportingplus.payment.data.jpa.repository;

import com.kaihei.esportingplus.payment.domain.entity.ExternalWithdrawOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 云账户订单DAO层——后续需要提供给python后台分页
 * @author chenzhenjun
 */
public interface ExternalWithdrawOrderRepository extends JpaRepository<ExternalWithdrawOrder, Long>, JpaSpecificationExecutor<ExternalWithdrawOrder> {

    /**
     * 根据业务订单号查找订单记录
     * @param outTradeNo
     * @return
     */
    ExternalWithdrawOrder findByOutTradeNo(String outTradeNo);

}

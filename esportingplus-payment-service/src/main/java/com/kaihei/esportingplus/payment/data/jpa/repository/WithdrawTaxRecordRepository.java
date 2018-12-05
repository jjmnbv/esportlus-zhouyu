package com.kaihei.esportingplus.payment.data.jpa.repository;

import com.kaihei.esportingplus.payment.domain.entity.WithdrawTaxRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 提现扣税记录DAO层
 * @author chenzhenjun
 */
public interface WithdrawTaxRecordRepository extends JpaRepository<WithdrawTaxRecord, Long>, JpaSpecificationExecutor<WithdrawTaxRecord> {

    /**
     * 通过订单号查询扣税记录
     * @param orderId
     * @return
     */
    WithdrawTaxRecord findByOrderId(String orderId);
}

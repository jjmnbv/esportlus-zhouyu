package com.kaihei.esportingplus.payment.data.jpa.repository;

import com.kaihei.esportingplus.payment.domain.entity.WithdrawAuditRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 提现审批记录DAO层
 *
 * @author chenzhenjun
 */
public interface WithdrawAuditRecordRepository extends JpaRepository<WithdrawAuditRecord, Long>, JpaSpecificationExecutor<WithdrawAuditRecord> {

    /**
     * 获取当日提现次数
     * @param uid
     * @param startDate
     * @param endDate
     * @param state
     * @return
     */
    Long countByUidAndCreateDateBetweenAndVerifyStateIn(String uid, Date startDate, Date endDate, Collection<String> state);

    /**
     * 分页查询APP端提现记录
     * @param uid
     * @param pageable
     * @return
     */
    List<WithdrawAuditRecord> findByUid(String uid, Pageable pageable);

    /**
     * 提现记录总数
     * @param uid
     * @return
     */
    Long countByUid(String uid);

    /**
     * 根据业务订单号查询提现审批记录
     * @param orderId
     * @return
     */
    WithdrawAuditRecord findByOrderId(String orderId);

}

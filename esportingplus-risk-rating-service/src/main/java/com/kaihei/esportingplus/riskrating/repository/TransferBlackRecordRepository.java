package com.kaihei.esportingplus.riskrating.repository;

import com.kaihei.esportingplus.riskrating.domain.entity.TransferBlackRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 提现黑名单-DAO层
 * @author chenzhenjun
 */
public interface TransferBlackRecordRepository extends JpaRepository<TransferBlackRecord, Long>,
        JpaSpecificationExecutor<TransferBlackRecord> {

    TransferBlackRecord findOneByUserId(String userId);

}

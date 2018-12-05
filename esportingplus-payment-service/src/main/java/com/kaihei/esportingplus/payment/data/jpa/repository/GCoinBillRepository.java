package com.kaihei.esportingplus.payment.data.jpa.repository;

import com.kaihei.esportingplus.payment.domain.entity.GCoinBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 暴鸡币账单流水数据仓库
 *
 * @author xiaolijun, haycco
 */
public interface GCoinBillRepository extends JpaRepository<GCoinBill, Long>, JpaSpecificationExecutor<GCoinBill> {
	
	Page<GCoinBill> findByUserId(String userId, Pageable pageable);
	
}

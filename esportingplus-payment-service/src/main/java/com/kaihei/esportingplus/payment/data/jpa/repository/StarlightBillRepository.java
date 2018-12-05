package com.kaihei.esportingplus.payment.data.jpa.repository;

import com.kaihei.esportingplus.payment.domain.entity.StarlightBalance;
import com.kaihei.esportingplus.payment.domain.entity.StarlightBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

/**
 * 暴击值账单流水数据仓库
 *
 * @author xiaolijun, haycco
 */
public interface StarlightBillRepository extends JpaRepository<StarlightBill, Long>,
        JpaSpecificationExecutor<StarlightBill> {

    Page<StarlightBill> findByUserId(String userId, Pageable pageable);

    StarlightBalance findOneByUserId(String userId);

    List<StarlightBill> findAllByUserIdAndTradeType(String userId, String tradeType);

    List<StarlightBill> findAllByUserIdAndTradeTypeAndCreateDateBetween(String userId, String tradeType, Date begin, Date end);

}

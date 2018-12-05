package com.kaihei.esportingplus.payment.data.jpa.repository;

import com.kaihei.esportingplus.payment.domain.entity.ExternalTradeBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

/**
 * 第三方支付流水表
 *
 * @author: tangtao
 **/
public interface ExternalTradeBillRepository extends JpaRepository<ExternalTradeBill, Long>,
        JpaSpecificationExecutor<ExternalTradeBill> {

    List<ExternalTradeBill> findByCreateDateBetweenOrderByCreateDateDesc(Date createDateBegin,
                                                                         Date createDateEnd);

    List<ExternalTradeBill> findByOrderIdAndCreateDateBetweenOrderByCreateDateDesc(Long orderId,
                                                                                   Date createDateBegin,
                                                                                   Date createDateEnd);

}

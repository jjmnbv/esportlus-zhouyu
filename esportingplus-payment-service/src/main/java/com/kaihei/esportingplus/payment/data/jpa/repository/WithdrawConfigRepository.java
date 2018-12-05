package com.kaihei.esportingplus.payment.data.jpa.repository;

import com.kaihei.esportingplus.payment.domain.entity.WithdrawConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 提现配置DAO层
 *
 * @author chenzhenjun
 */
public interface WithdrawConfigRepository extends JpaRepository<WithdrawConfig, Long> {
}

package com.kaihei.esportingplus.riskrating.repository;

import com.kaihei.esportingplus.riskrating.domain.entity.RiskDeviceUserRechargeLog;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RiskDeviceUserRechargeLogRepository extends JpaRepository<RiskDeviceUserRechargeLog, Long> ,
        JpaSpecificationExecutor<RiskDeviceUserRechargeLog> {
}

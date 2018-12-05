package com.kaihei.esportingplus.riskrating.repository;

import com.kaihei.esportingplus.riskrating.domain.entity.RiskDeviceWhite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RiskDeviceWhiteRepository extends JpaRepository<RiskDeviceWhite, Long>,JpaSpecificationExecutor<RiskDeviceWhite> {
    RiskDeviceWhite findByDeviceNo(String deviceNo);
}

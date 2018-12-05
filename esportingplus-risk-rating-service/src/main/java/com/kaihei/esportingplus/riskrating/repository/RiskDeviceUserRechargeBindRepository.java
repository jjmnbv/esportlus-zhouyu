package com.kaihei.esportingplus.riskrating.repository;

import com.kaihei.esportingplus.riskrating.domain.entity.RiskDeviceUserRechargeBind;
import com.kaihei.esportingplus.riskrating.domain.entity.RiskDict;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiskDeviceUserRechargeBindRepository extends JpaRepository<RiskDeviceUserRechargeBind, Long> {
    List<RiskDeviceUserRechargeBind> findByDeviceNo(String deviceNo);
}

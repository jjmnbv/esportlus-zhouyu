package com.kaihei.esportingplus.riskrating.repository;

import com.kaihei.esportingplus.riskrating.domain.entity.RiskControlNextdata;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 数美记录-DAO层
 * @author chenzhenjun
 */
public interface RiskControlNextdataRepository extends JpaRepository<RiskControlNextdata, Long> {

    RiskControlNextdata findByUidAndDeviceIdAndAction(String uid, String deviceId, int action);

}

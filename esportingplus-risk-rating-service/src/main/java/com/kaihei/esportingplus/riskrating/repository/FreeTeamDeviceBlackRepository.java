package com.kaihei.esportingplus.riskrating.repository;

import com.kaihei.esportingplus.riskrating.domain.entity.FreeTeamDeviceBlack;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 免费车队-风控服务-恶意设备DAO层
 * @author chenzhenjun
 */
public interface FreeTeamDeviceBlackRepository extends JpaRepository<FreeTeamDeviceBlack, Long> {

    /**
     * 查询恶意设备
     * @param deviceId
     * @return
     */
    long countByDeviceId(String deviceId);
}

package com.kaihei.esportingplus.riskrating.repository;

import com.kaihei.esportingplus.riskrating.domain.entity.FreeTeamDeviceBind;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 免费车队-风控服务-数美设备绑定DAO层
 * @author chenzhenjun
 */
public interface FreeTeamDeviceBindRepository extends JpaRepository<FreeTeamDeviceBind, Long> {

    /**
     * 查询数美id绑定记录
     * @param deviceId
     * @return
     */
    long countByDeviceId(String deviceId);

}

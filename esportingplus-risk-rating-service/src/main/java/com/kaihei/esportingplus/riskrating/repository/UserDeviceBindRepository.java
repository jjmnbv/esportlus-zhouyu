package com.kaihei.esportingplus.riskrating.repository;

import com.kaihei.esportingplus.riskrating.domain.entity.UserDeviceBind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 账户数美设备绑定关系-DAO层
 * @author chenzhenjun
 */
public interface UserDeviceBindRepository extends JpaRepository<UserDeviceBind, Long>,
        JpaSpecificationExecutor<UserDeviceBind> {

    /**
     * 通过userId及deviceId查询绑定关系
     * @param userId
     * @param deviceId
     * @return
     */
    UserDeviceBind findOneByUserIdAndDeviceId(String userId, String deviceId);
}

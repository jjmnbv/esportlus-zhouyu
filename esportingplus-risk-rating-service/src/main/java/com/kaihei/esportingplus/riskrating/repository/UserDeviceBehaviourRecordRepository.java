package com.kaihei.esportingplus.riskrating.repository;

import com.kaihei.esportingplus.riskrating.domain.entity.UserDeviceBehaviourRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * 用户设备行为记录-DAO层
 * @author chenzhenjun
 */
public interface UserDeviceBehaviourRecordRepository extends JpaRepository<UserDeviceBehaviourRecord, Long> {

    /**
     * 校验数美设备注册上限
     * @param deviceId
     * @return
     */
    long countByRegisterDeviceId(String deviceId);

    /**
     * 检验数美设备登陆上限——可以重复登陆
     * @param deviceId
     * @return
     */
    long countByLoginDeviceIdAndUserIdNot(String deviceId, String userId);

    /**
     * 根据用户查询注册登陆记录
     * @param userId
     * @return
     */
    UserDeviceBehaviourRecord findByUserId(String userId);

    /**
     * 根据userIds 查询deviceIds
     * @param userIds
     * @return
     */
    List<UserDeviceBehaviourRecord> findByUserIdIn(Collection<String> userIds);

}

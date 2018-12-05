package com.kaihei.esportingplus.riskrating.repository;

import com.kaihei.esportingplus.riskrating.domain.entity.FreeTeamChance;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 免费车队-风控服务-数美id上车次数统计DAO层
 * @author chenzhenjun
 */
public interface FreeTeamChanceRepository extends JpaRepository<FreeTeamChance, Long> {

    /**
     * 获取指定日期次数统计
     * @param deviceId
     * @param date
     * @return
     */
    FreeTeamChance findByDeviceIdAndStatisticsDate(String deviceId, String date);
}

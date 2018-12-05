package com.kaihei.esportingplus.riskrating.repository;

import com.kaihei.esportingplus.riskrating.domain.entity.FreeTeamRecord;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 免费车队-风控服务-上车机会DAO层
 * @author chenzhenjun
 */
public interface FreeTeamRecordRepository extends JpaRepository<FreeTeamRecord, Long> {

    /**
     * 查询数美id一天和一周的上车次数
     * @param var
     * @return
     */
    long count(Specification<FreeTeamRecord> var);

    /**
     * 查询数美id一天和一周的上车次数
     * @param deviceId
     * @param startDate
     * @param endDate
     * @return
     */
    long countByDeviceIdAndEndDateBetween(String deviceId, LocalDateTime startDate, LocalDateTime endDate);

}

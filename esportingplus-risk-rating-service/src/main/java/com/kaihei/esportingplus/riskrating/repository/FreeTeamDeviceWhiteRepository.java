package com.kaihei.esportingplus.riskrating.repository;

import com.kaihei.esportingplus.riskrating.domain.entity.FreeTeamDeviceWhite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FreeTeamDeviceWhiteRepository extends JpaRepository<FreeTeamDeviceWhite, Long>, JpaSpecificationExecutor<FreeTeamDeviceWhite> {

    FreeTeamDeviceWhite findOneByDeviceId(String deviceId);
}

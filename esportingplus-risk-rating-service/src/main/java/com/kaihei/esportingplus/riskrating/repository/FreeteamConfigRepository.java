package com.kaihei.esportingplus.riskrating.repository;

import com.kaihei.esportingplus.riskrating.domain.entity.FreeTeamConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 免费车队-风控服务-配置参数DAO层
 * @author chenzhenjun
 */
public interface FreeteamConfigRepository extends JpaRepository<FreeTeamConfig, Long> {

    /**
     * 通过模块code获取配置信息
     * @param moduleCode
     * @return
     */
    FreeTeamConfig findOneByModuleCode(String moduleCode);
}

package com.kaihei.esportingplus.riskrating.repository;

import com.kaihei.esportingplus.riskrating.domain.entity.ImMachineConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 风控服务-IM防骚扰及虚拟机判断配置参数—DAO层
 * @author chenzhenjun
 */
public interface ImMachineConfigRepository extends JpaRepository<ImMachineConfig, Long> {

}

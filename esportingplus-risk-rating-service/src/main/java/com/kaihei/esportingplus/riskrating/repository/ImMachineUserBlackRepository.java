package com.kaihei.esportingplus.riskrating.repository;

import com.kaihei.esportingplus.riskrating.domain.entity.ImMachineUserBlack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
/**
 * 风控服务-IM防骚扰及虚拟机判断需求—用户黑名单DAO层
 *
 * @author chenzhenjun
 */
public interface ImMachineUserBlackRepository extends JpaRepository<ImMachineUserBlack, Long>,
        JpaSpecificationExecutor<ImMachineUserBlack> {
}

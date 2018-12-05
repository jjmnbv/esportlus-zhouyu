package com.kaihei.esportingplus.riskrating.declare;

import com.kaihei.esportingplus.riskrating.domain.entity.Recharge;

/**
 * 可增长风险
 */
public interface IncreaseAbleRiskDeclare {

    /**
     * 增加风险
     */
    void increaseRisk(Recharge recharge);
}

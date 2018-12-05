package com.kaihei.esportingplus.riskrating.declare;

import com.kaihei.esportingplus.riskrating.api.params.RechargeRiskParams;
import com.kaihei.esportingplus.riskrating.api.vo.RiskBaseResponse;

/**
 * 可阻止充值风险
 */
public interface PreventAbleRiskDeclare {

    /**
     * 判断是否有风险
     */
    RiskBaseResponse hasRisk(RechargeRiskParams rechargeRiskParams);
}

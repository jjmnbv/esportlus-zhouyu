package com.kaihei.esportingplus.riskrating.declare.impl;

import com.kaihei.esportingplus.riskrating.api.enums.RiskEnum;
import com.kaihei.esportingplus.riskrating.api.params.RechargeRiskParams;
import com.kaihei.esportingplus.riskrating.api.vo.RiskBaseResponse;
import com.kaihei.esportingplus.riskrating.declare.SimpleRiskDeclare;
import com.kaihei.esportingplus.riskrating.service.impl.RechargeFreezeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserFreezeRiskDeclare extends SimpleRiskDeclare {

    @Autowired
    private RechargeFreezeService rechargeFreezeService;

    /**
     * 判断是否有风险
     */
    @Override
    public RiskBaseResponse hasRisk(RechargeRiskParams rechargeRiskParams) {
        boolean rechargeFreezed = rechargeFreezeService
                .isRechargeFreezed(rechargeRiskParams.getUid());
        log.info("用户：{} 充值冻结状态为：{}", rechargeRiskParams.getUid(), rechargeFreezed ? "被冻结" : "未被冻结");
        if (!rechargeFreezed) {
            return null;
        }

        return RiskBaseResponse.risk(RiskEnum.USER_RECHARGE_FREEZED);
    }
}

package com.kaihei.esportingplus.riskrating.declare.impl;

import com.kaihei.esportingplus.riskrating.api.enums.RiskEnum;
import com.kaihei.esportingplus.riskrating.api.params.RechargeRiskParams;
import com.kaihei.esportingplus.riskrating.api.vo.RiskBaseResponse;
import com.kaihei.esportingplus.riskrating.declare.SimpleRiskDeclare;
import org.springframework.stereotype.Component;

@Component
public class RechargeForbiddenRiskDeclare extends SimpleRiskDeclare {


    /**
     * 判断是否有风险
     */
    @Override
    public RiskBaseResponse hasRisk(RechargeRiskParams rechargeRiskParams) {
        Integer forbidden = findValueByItemCode(
                "PLATFORM_IOS_RECHARGE_FORBIDDEN",
                it -> Integer.parseInt(it.getItemValue()));
        log.info("平台IOS充值功能: {}", forbidden == 0 ? "可充值" : "被禁止");
        if (forbidden == 1) {
            return RiskBaseResponse.risk(RiskEnum.PLATFORM_IOS_RECHARGE_FORBIDDEN);
        }
        return null;
    }
}

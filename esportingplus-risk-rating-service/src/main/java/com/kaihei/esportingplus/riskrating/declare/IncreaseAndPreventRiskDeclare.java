package com.kaihei.esportingplus.riskrating.declare;

import com.kaihei.esportingplus.riskrating.api.enums.RiskEnum;
import com.kaihei.esportingplus.riskrating.api.params.RechargeRiskParams;
import com.kaihei.esportingplus.riskrating.api.vo.RiskBaseResponse;
import com.kaihei.esportingplus.riskrating.domain.entity.Recharge;
import com.kaihei.esportingplus.riskrating.domain.entity.RiskDict;
import java.util.Optional;

/**
 * @author 谢思勇
 * @date 2018年10月22日 11:00:49
 */
public abstract class IncreaseAndPreventRiskDeclare extends IncreaseRiskDeclare implements
        PreventAbleRiskDeclare {

    /**
     * 判断是否有风险
     */
    @Override
    public RiskBaseResponse hasRisk(RechargeRiskParams rechargeRiskParams) {
        //不需要检验的情况
        if (getOnRiskWarn() == null) {
            return null;
        }

        //已充值
        Long recharged = Optional
                .ofNullable(cacheManager.get(getIncrKey(rechargeRiskParams), Long.class))
                .orElse(0L);
        //风险阀值
        RiskDict thresholdDict = findValueByItemCode(getThresholdItemCode());

        if (thresholdDict == null) {
            return null;
        }

        long threshold = Long.parseLong(thresholdDict.getItemValue());
        log.info("风控名:{} \t用户:{}  \t设备:{} \t阀值{} \t充值金额:{} \t当前已充值{}", thresholdDict.getItemName(),
                rechargeRiskParams.getUid(), rechargeRiskParams.getDeviceNo(),
                threshold / 100, rechargeRiskParams.getAmount() / 100, recharged / 100);

        //检验通过
        if (recharged + rechargeRiskParams.getAmount() <= threshold) {
            return null;
        }

        return RiskBaseResponse.risk(getOnRiskWarn());
    }


    /**
     * 风险增长后
     *
     * 什么都不做
     */
    @Override
    protected void handleRisk(String thresholdName, Long threshold, long incred,
            Recharge recharge) {

    }

    /**
     * 有风险时，返回前端的View
     *
     * Null 前端充值不参与校验
     */
    protected abstract RiskEnum getOnRiskWarn();
}

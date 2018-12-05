package com.kaihei.esportingplus.riskrating.declare;

import com.kaihei.esportingplus.riskrating.bean.DingdingWarnBean;
import com.kaihei.esportingplus.riskrating.domain.entity.Recharge;
import com.kaihei.esportingplus.riskrating.domain.entity.RiskDict;

public abstract class IncreaseAndDingWarningRiskDeclare extends IncreaseRiskDeclare {

    /**
     * 风险增长后
     */
    @Override
    protected void handleRisk(String thresholdName, Long threshold, long incred,
            Recharge recharge) {
        warning2DingdingIfNeed(incred, recharge, threshold, thresholdName);
    }

    /**
     * 如果需要、就发钉钉预警
     */
    private void warning2DingdingIfNeed(long incred, Recharge recharge, Long threshold,
            String thresholdName) {
        //没配置提醒文本 直接返回
        RiskDict riskWarningDict = findValueByItemCode(getWarningTextItemCode());
        if (riskWarningDict == null) {
            return;
        }

        DingdingWarnBean warnBean = DingdingWarnBean.builder()
                .name(thresholdName)
                .threshold(threshold / 100)
                .recharged(incred / 100)
                .recharge(recharge.getAmount() / 100).build();

        riskRatingService.send(riskWarningDict.getItemValue(), warnBean);
    }


    /**
     * 字典：钉钉预警文本的ItemCode
     *
     * Null不会发消息
     */
    protected abstract String getWarningTextItemCode();
}

package com.kaihei.esportingplus.riskrating.declare.impl;

import com.kaihei.esportingplus.riskrating.api.enums.RiskEnum;
import com.kaihei.esportingplus.riskrating.declare.IncreaseAndPreventRiskDeclare;
import com.kaihei.esportingplus.riskrating.domain.entity.Recharge;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
public class UserMonthRechargeRiskDeclare extends IncreaseAndPreventRiskDeclare {

    /**
     * 由子类实现
     *
     * 定义：Redis上的Key
     */
    @Override
    protected String getIncrKey(Recharge recharge) {
        return String.format("riskrating:ios:month:%s:user:%s", LocalDate.now().format(
                DateTimeFormatter.ofPattern("yyyyMM")), recharge.getUid());
    }

    /**
     * 字典：风控值阀值的 itemCode
     */
    @Override
    protected String getThresholdItemCode() {
        return "USER_MONTH_MAX_RECHARGE";
    }

    /**
     * 有风险时，返回前端的View
     *
     * Null 前端充值不参与校验
     */
    @Override
    protected RiskEnum getOnRiskWarn() {
        return RiskEnum.USER_MONTH_RECHARGE_AMOUNT_LIMIT;
    }

    /**
     * 过期时间(秒)
     */
    @Override
    protected Integer getExpireSeconds() {
        return 31 * 24 * 60 * 60;
    }
}

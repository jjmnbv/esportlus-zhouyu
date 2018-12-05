package com.kaihei.esportingplus.riskrating.declare.impl;

import com.kaihei.esportingplus.riskrating.declare.IncreaseAndDingWarningRiskDeclare;
import com.kaihei.esportingplus.riskrating.domain.entity.Recharge;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
public class AllUserDayRechargeRiskDeclare extends IncreaseAndDingWarningRiskDeclare {

    /**
     * 由子类实现
     *
     * 定义：Redis上的Key
     */
    @Override
    protected String getIncrKey(Recharge recharge) {
        String yyyyMMdd = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return String.format("riskrating:ios:allusers:date:%s", yyyyMMdd);
    }

    /**
     * 字典：风控值阀值的 itemCode
     */
    @Override
    protected String getThresholdItemCode() {
        return "ALL_USERS_DAY_MAX_RECHARGE";
    }

    /**
     * 字典：钉钉预警文本的ItemCode
     *
     * Null不会发消息
     */
    @Override
    protected String getWarningTextItemCode() {
        return "ALL_USERS_OVER_RECHARGE_WARNING_JSON";
    }
}

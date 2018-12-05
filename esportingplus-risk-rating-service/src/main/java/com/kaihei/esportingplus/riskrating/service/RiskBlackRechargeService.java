package com.kaihei.esportingplus.riskrating.service;

import com.kaihei.esportingplus.riskrating.api.params.RechargeRiskParams;
import com.kaihei.esportingplus.riskrating.api.params.RiskBlackCheckConsumerParams;
import com.kaihei.esportingplus.riskrating.api.vo.RiskBaseResponse;

/**
 * 用户充值黑名单风控相关服务接口
 *
 * @author 谢思勇
 */
public interface RiskBlackRechargeService {

    /**
     * 充值后，MQ消费消息时调用，用户存入记录数据，及发告警
     * @param rechargeRiskParams
     */
    public void rechargeRiskAlerm(RiskBlackCheckConsumerParams rechargeRiskParams);

    /**
     * 充值前调用，用户判断能否进行充值
     * @param rechargeRiskParams
     * @return
     */
    public RiskBaseResponse checkRechargeStatus(RechargeRiskParams rechargeRiskParams);
}

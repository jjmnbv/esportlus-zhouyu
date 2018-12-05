package com.kaihei.esportingplus.riskrating.declare;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.riskrating.api.params.RechargeRiskParams;
import com.kaihei.esportingplus.riskrating.domain.entity.Recharge;
import com.kaihei.esportingplus.riskrating.domain.entity.RiskDict;
import com.kaihei.esportingplus.riskrating.service.impl.RiskRatingService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 谢思勇
 */
public abstract class IncreaseRiskDeclare extends GeneralRiskDeclare implements
        IncreaseAbleRiskDeclare {

    @Autowired
    protected RiskRatingService riskRatingService;

    protected CacheManager cacheManager = CacheManagerFactory.create();

    /**
     * 增加风险
     */
    @Override
    public void increaseRisk(Recharge recharge) {
        String incrKey = getIncrKey(recharge);
        if (incrKey == null) {
            return;
        }

        long incred = cacheManager.incrBy(incrKey, recharge.getAmount());
        if (recharge.getAmount().equals(incred)) {
            //设置超时时间
            cacheManager.expire(incrKey, getExpireSeconds());
        }
        //没有或没到风险阀值 直接返回
        RiskDict thresholdDict = findValueByItemCode(getThresholdItemCode());
        Long threshold = Optional.ofNullable(thresholdDict).map(it -> Long
                .parseLong(it.getItemValue())).orElse(null);

        if (threshold == null) {
            return;
        }

        log.info(
                "风控名 \t-> \t{} \t阀值 \t-> \t{} \t充值金额 \t-> \t{} \t当前已充值 \t-> \t{}",
                thresholdDict.getItemName(),
                threshold / 100, recharge.getAmount() / 100, incred / 100);
        if (incred <= threshold) {
            return;
        }
        //判定有风险才会调用 after
        handleRisk(thresholdDict.getItemName(), threshold, incred, recharge);
    }


    /**
     * 复写参数
     */
    protected String getIncrKey(RechargeRiskParams rechargeRiskParams) {
        return getIncrKey(rechargeRiskParams.cast(Recharge.class));
    }

    /**
     * 由子类实现
     *
     * 定义：Redis上的Key
     */
    protected abstract String getIncrKey(Recharge recharge);

    /**
     * 过期时间(秒)
     */
    protected Integer getExpireSeconds() {
        return 25 * 60 * 60;
    }

    /**
     * 风险增长后
     */
    protected abstract void handleRisk(String thresholdName, Long threshold, long incred,
            Recharge recharge);

    /**
     * 字典：风控值阀值的 itemCode
     */
    protected abstract String getThresholdItemCode();
}



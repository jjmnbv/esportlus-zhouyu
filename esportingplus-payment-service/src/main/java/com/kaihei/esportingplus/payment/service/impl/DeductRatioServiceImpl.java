package com.kaihei.esportingplus.payment.service.impl;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.payment.api.enums.DeductRatioEnum;
import com.kaihei.esportingplus.payment.api.enums.DeductRatioStatus;
import com.kaihei.esportingplus.payment.data.jpa.repository.DeductRatioSettingRepository;
import com.kaihei.esportingplus.payment.domain.entity.DeductRatioSetting;
import com.kaihei.esportingplus.payment.service.DeductRatioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 系统抽成比例参数服务实现类
 *
 * @author haycco
 */
@Service
public class DeductRatioServiceImpl implements DeductRatioService {

    private static final Logger logger = LoggerFactory.getLogger(DeductRatioServiceImpl.class);
    private static final CacheManager cacheManager = CacheManagerFactory.create();

    @Autowired
    private DeductRatioSettingRepository deductRatioSettingRepository;

    @Override
    public DeductRatioSetting queryCalcOrderIncomeRatio() {
        DeductRatioSetting calcOrderRatioSetting = this.queryOrInitRatio(DeductRatioEnum.CALC_ORDER);
        return calcOrderRatioSetting;
    }

    @Override
    public DeductRatioSetting queryWithdrawTaxRatio() {
        DeductRatioSetting withdrawTaxRatioSetting = this.queryOrInitRatio(DeductRatioEnum.WITHDRAW);
        return withdrawTaxRatioSetting;
    }

    @Override
    public DeductRatioSetting updateCalcOrderIncomeRatio(float ratio) {
        if(ratio>1 || ratio<0) {
            throw new BusinessException(BizExceptionEnum.INVALID_DEDUCT_RATIO_VALUE);
        }
        DeductRatioSetting calcOrderRatioSetting = this.queryOrInitRatio(DeductRatioEnum.CALC_ORDER);
        calcOrderRatioSetting.setRatio(ratio);
        calcOrderRatioSetting = deductRatioSettingRepository.save(calcOrderRatioSetting);
        this.refreshRedisDeductRatioSetting(calcOrderRatioSetting);
        return calcOrderRatioSetting;
    }

    @Override
    public DeductRatioSetting updateCalcOrderIncomeRatio(float ratio, DeductRatioStatus deductRatioStatus) {
        if(ratio>1 || ratio<0) {
            throw new BusinessException(BizExceptionEnum.INVALID_DEDUCT_RATIO_VALUE);
        }
        DeductRatioSetting calcOrderRatioSetting = this.queryOrInitRatio(DeductRatioEnum.CALC_ORDER);
        calcOrderRatioSetting.setRatio(ratio);
        calcOrderRatioSetting.setState(deductRatioStatus.toString());
        calcOrderRatioSetting = deductRatioSettingRepository.save(calcOrderRatioSetting);
        this.refreshRedisDeductRatioSetting(calcOrderRatioSetting);
        return calcOrderRatioSetting;
    }

    @Override
    public DeductRatioSetting updateWithdrawTaxRatio(float ratio) {
        if(ratio>1 || ratio<0) {
            throw new BusinessException(BizExceptionEnum.INVALID_DEDUCT_RATIO_VALUE);
        }
        DeductRatioSetting withdrawTaxRatioSetting = this.queryOrInitRatio(DeductRatioEnum.WITHDRAW);
        withdrawTaxRatioSetting.setRatio(ratio);
        withdrawTaxRatioSetting = deductRatioSettingRepository.save(withdrawTaxRatioSetting);
        this.refreshRedisDeductRatioSetting(withdrawTaxRatioSetting);
        return withdrawTaxRatioSetting;
    }

    @Override
    public DeductRatioSetting updateWithdrawTaxRatio(float ratio, DeductRatioStatus deductRatioStatus) {
        if(ratio>1 || ratio<0) {
            throw new BusinessException(BizExceptionEnum.INVALID_DEDUCT_RATIO_VALUE);
        }
        DeductRatioSetting withdrawTaxRatioSetting = this.queryOrInitRatio(DeductRatioEnum.WITHDRAW);
        withdrawTaxRatioSetting.setRatio(ratio);
        withdrawTaxRatioSetting.setState(deductRatioStatus.toString());
        withdrawTaxRatioSetting = deductRatioSettingRepository.save(withdrawTaxRatioSetting);
        this.refreshRedisDeductRatioSetting(withdrawTaxRatioSetting);
        return withdrawTaxRatioSetting;
    }

    @Override
    public boolean enableDeductRatio(DeductRatioEnum deductRatioEnum) {
        DeductRatioSetting deductRatioSetting = this.queryOrInitRatio(deductRatioEnum);
        deductRatioSetting.setState(DeductRatioStatus.ENABLE.toString());
        deductRatioSetting = deductRatioSettingRepository.save(deductRatioSetting);
        if(deductRatioSetting != null && deductRatioSetting.getId() != null && deductRatioSetting.getId() != 0) {
            this.refreshRedisDeductRatioSetting(deductRatioSetting);
            return true;
        }
        return false;
    }

    @Override
    public boolean closeDeductRatio(DeductRatioEnum deductRatioEnum) {
        DeductRatioSetting deductRatioSetting = this.queryOrInitRatio(deductRatioEnum);
        deductRatioSetting.setState(DeductRatioStatus.DISABLE.toString());
        deductRatioSetting = deductRatioSettingRepository.save(deductRatioSetting);
        if(deductRatioSetting != null && deductRatioSetting.getId() != null && deductRatioSetting.getId() != 0) {
            this.refreshRedisDeductRatioSetting(deductRatioSetting);
            return true;
        }
        return false;
    }

    @Override
    public Integer getCalcAfterDeductRatioValue(Integer value, DeductRatioEnum deductRatioEnum) {
        DeductRatioSetting deductRatioSetting = this.queryOrInitRatio(deductRatioEnum);
        BigDecimal ratio;
        if(DeductRatioStatus.DISABLE == DeductRatioStatus.valueOf(deductRatioSetting.getState())){
            //禁用抽成后，默认为 0
            ratio = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            ratio = new BigDecimal(deductRatioSetting.getRatio()).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        BigDecimal total = new BigDecimal(value);
        Integer result = total.divide(total.multiply(ratio)).intValue();
        return result;
    }

    /**
     * 刷新抽成的缓存配置
     *
     * @param deductRatioSetting
     */
    private void refreshRedisDeductRatioSetting(DeductRatioSetting deductRatioSetting) {
        String deductRedisKey = String.format(RedisKey.PAYMENT_DEDUCT_RATIO_PREFIX, deductRatioSetting.getTag().toLowerCase());
        try {
            cacheManager.del(deductRedisKey);
            cacheManager.set(deductRedisKey, deductRatioSetting, -1);
        } catch (Exception ex) {
            logger.warn("抽成比例缓存更新失败{}", ex.getMessage());
        }
    }
    /**
     * 查询或初始化抽成比率
     *
     * @param deductRatioEnum 抽成类型枚举值
     */
    private DeductRatioSetting queryOrInitRatio(DeductRatioEnum deductRatioEnum){
        DeductRatioSetting deductRatioSetting = cacheManager.get(String.format(RedisKey.PAYMENT_DEDUCT_RATIO_PREFIX, deductRatioEnum.toString().toLowerCase()), DeductRatioSetting.class);
        if(deductRatioSetting == null) {
            deductRatioSetting = deductRatioSettingRepository.findByTag(deductRatioEnum.toString());
            if (DeductRatioEnum.CALC_ORDER.equals(deductRatioEnum)) {
                if(deductRatioSetting == null) {
                    //创建一个默认的数据
                    deductRatioSetting = new DeductRatioSetting();
                    deductRatioSetting.setTag(DeductRatioEnum.CALC_ORDER.toString());
                    deductRatioSetting.setDescription("订单结算抽成比率");
                    deductRatioSetting.setRatio(0.00f);
                    deductRatioSetting.setState(DeductRatioStatus.ENABLE.toString());
                    deductRatioSetting = deductRatioSettingRepository.save(deductRatioSetting);
                    logger.warn("未设置默认的订单结算抽成比例数据，系统直接初始订单结算抽成比例默认数据{}", deductRatioSetting);
                }
                cacheManager.set(String.format(RedisKey.PAYMENT_DEDUCT_RATIO_PREFIX, deductRatioEnum.toString().toLowerCase()), deductRatioSetting, -1);
            } else if (DeductRatioEnum.WITHDRAW.equals(deductRatioEnum)) {
                if(deductRatioSetting == null) {
                    //创建一个默认的数据
                    deductRatioSetting = new DeductRatioSetting();
                    deductRatioSetting.setTag(DeductRatioEnum.WITHDRAW.toString());
                    deductRatioSetting.setDescription("提现税率抽成比率");
                    deductRatioSetting.setRatio(0.00f);
                    deductRatioSetting.setState(DeductRatioStatus.ENABLE.toString());
                    deductRatioSetting = deductRatioSettingRepository.save(deductRatioSetting);
                    logger.warn("未设置默认的提现税率抽成比例数据，系统直接初始提现税率抽成比例默认数据{}", deductRatioSetting);
                }
                cacheManager.set(String.format(RedisKey.PAYMENT_DEDUCT_RATIO_PREFIX, deductRatioEnum.toString().toLowerCase()), deductRatioSetting, -1);
            }
        }
        logger.debug("查询分成比例{}", deductRatioSetting);
        return deductRatioSetting;
    }
}

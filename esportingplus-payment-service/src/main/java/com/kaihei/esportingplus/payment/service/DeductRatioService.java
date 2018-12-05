package com.kaihei.esportingplus.payment.service;

import com.kaihei.esportingplus.payment.api.enums.DeductRatioEnum;
import com.kaihei.esportingplus.payment.api.enums.DeductRatioStatus;
import com.kaihei.esportingplus.payment.domain.entity.DeductRatioSetting;

/**
 * 系统抽成比例参数服务接口
 *
 * @author haycco
 */
public interface DeductRatioService {
    /**
     * 查询订单结算抽成比例
     */
    DeductRatioSetting queryCalcOrderIncomeRatio();
    /**
     * 查询提现税率抽成比例
     */
    DeductRatioSetting queryWithdrawTaxRatio();
    /**
     * 更新订单结算抽成比例
     *
     * @param ratio 比率值 < 1
     */
    DeductRatioSetting updateCalcOrderIncomeRatio(float ratio);
    /**
     * 更新订单结算抽成比例及状态
     *
     * @param ratio 比率值 < 1
     * @param deductRatioStatus 启用/禁用
     */
    DeductRatioSetting updateCalcOrderIncomeRatio(float ratio, DeductRatioStatus deductRatioStatus);
    /**
     * 更新提现税率抽成比例
     *
     *  @param ratio 比率值 < 1
     */
    DeductRatioSetting updateWithdrawTaxRatio(float ratio);
    /**
     * 更新提现税率抽成比例及状态
     *
     * @param ratio 比率值 < 1
     * @param deductRatioStatus 启用/禁用
     */
    DeductRatioSetting updateWithdrawTaxRatio(float ratio, DeductRatioStatus deductRatioStatus);
    /**
     * 启用抽成比例
     *
     * @param deductRatioEnum 抽成比例枚举值
     */
    boolean enableDeductRatio(DeductRatioEnum deductRatioEnum);
    /**
     * 关闭抽成比例
     *
     * @param deductRatioEnum 抽成比例枚举值
     */
    boolean closeDeductRatio(DeductRatioEnum deductRatioEnum);

    /**
     * 获取抽取分成后的计算数值
     * @param value 原始值
     * @param deductRatioEnum 抽成类型
     * @return
     */
    Integer getCalcAfterDeductRatioValue(Integer value, DeductRatioEnum deductRatioEnum);

}

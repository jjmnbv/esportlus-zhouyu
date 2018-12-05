package com.kaihei.esportingplus.payment.service;

import com.kaihei.esportingplus.payment.api.vo.WalletsVO;
import com.kaihei.esportingplus.payment.domain.entity.*;

/**
 * 用户钱包（暴鸡币 + 暴击值）流水明细服务接口
 *
 * @author haycco, tangtao
 */
public interface WalletService {

    /**
     * 保存支付订单流水记录
     *
     * @param order 支付订单信息
     * @return true: 保存成功、false: 保存失败
     */
    boolean saveHistoryDetails(GCoinPaymentOrder order);

    /**
     * 保存充值订单流水记录
     *
     * @param order 充值订单信息
     * @return true: 保存成功、false: 保存失败
     */
    boolean saveHistoryDetails(GCoinRechargeOrder order);

    /**
     * 保存退款订单流水记录
     *
     * @param order 退款订单信息
     * @return true: 保存成功、false: 保存失败
     */
    boolean saveHistoryDetails(GCoinRefundOrder order);

    /**
     * 保存打赏订单流水记录
     *
     * @param order 打赏订单信息
     * @return true: 保存成功、false: 保存失败
     */
    boolean saveHistoryDetails(GCoinRewardOrder order);

    /**
     * 保存收益分成订单流水记录
     *
     * @param order 收益分成订单信息
     * @return true: 保存成功、false: 保存失败
     */
    boolean saveHistoryDetails(OrderIncome order);

    /**
     * 保存提现订单流水记录
     *
     * @param order 提现订单信息
     * @return true: 保存成功、false: 保存失败
     */
    boolean saveHistoryDetails(WithdrawOrder order);

    /**
     * 保存暴鸡币和暴击值扣款订单流水记录
     *
     * @param order 暴鸡币和暴击值扣款订单信息
     * @return true: 保存成功、false: 保存失败
     */
    boolean saveHistoryDetails(DeductOrder order);

    /**
     * 获取暴击值和暴鸡币余额
     *
     * @param userId 用户ID
     */
    WalletsVO getBalance(String userId);
}

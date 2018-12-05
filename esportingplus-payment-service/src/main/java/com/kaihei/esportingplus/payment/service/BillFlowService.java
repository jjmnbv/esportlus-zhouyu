package com.kaihei.esportingplus.payment.service;

import com.kaihei.esportingplus.payment.api.params.BillQueryParams;
import com.kaihei.esportingplus.payment.api.vo.WalletBillsVO;
import com.kaihei.esportingplus.payment.domain.entity.*;

import java.util.Date;
import java.util.List;

/**
 * 订单类流水接口
 *
 * @Author: xiaolijun, haycco
 */
public interface BillFlowService {

    /**
     * 异步保存流水明细
     *
     * @param order 订单信息
     * <pre>
     * {@link GCoinPaymentOrder}<br>
     * {@link GCoinRechargeOrder}<br>
     * {@link GCoinRefundOrder}<br>
     * {@link GCoinRewardOrder}<br>
     * {@link OrderIncome}<br>
     * {@link WithdrawOrder}
     * </pre>
     */
    public <T> void asyncSaveRecord(T order);

    /**
     * 保存流水明细
     *
     * @param order 订单信息
     * <pre>
     * {@link GCoinPaymentOrder}<br>
     * {@link GCoinRechargeOrder}<br>
     * {@link GCoinRefundOrder}<br>
     * {@link GCoinRewardOrder}<br>
     * {@link OrderIncome}<br>
     * {@link WithdrawOrder}
     * </pre>
     * @return true: 保存成功、false: 保存失败
     */
    public <T> boolean saveRecord(T order);

    /**
     * 获取暴击值流水
     */
    public List<WalletBillsVO> getStarlightBill(BillQueryParams params);

    /**
     * /获取暴鸡币流水接口
     */
    public List<WalletBillsVO> getGCoinBill(BillQueryParams params);


    /**
     * 获取用户累计暴击值
     */
    Integer getAllStarlight(String userId, String tradeType);

    /**
     * 获取用户累计暴击值
     */
    Integer getAllStarlight(String userId, String tradeType, Date beginDate, Date endDate);

}

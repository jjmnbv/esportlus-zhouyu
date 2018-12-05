package com.kaihei.esportingplus.payment.service.impl;

import com.kaihei.esportingplus.common.algorithm.SnowFlake;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.payment.api.enums.*;
import com.kaihei.esportingplus.payment.api.vo.WalletsVO;
import com.kaihei.esportingplus.payment.data.jpa.repository.GCoinBalanceRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.GCoinBillRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.StarlightBalanceRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.StarlightBillRepository;
import com.kaihei.esportingplus.payment.domain.entity.*;
import com.kaihei.esportingplus.payment.service.WalletService;
import com.kaihei.esportingplus.payment.util.AccountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 打赏订单流水记录服务实现类
 *
 * @author haycco, tangtao
 */
@Service
public class WalletServiceImpl implements WalletService {

    private static final Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);
    @Autowired
    private SnowFlake snowFlake;
    @Autowired
    private GCoinBillRepository gCoinBillRepository;
    @Autowired
    private StarlightBillRepository starlightBillRepository;
    private static final BigDecimal NUM_ZERO = new BigDecimal("0.00");
    private static final BigDecimal NUM_100 = new BigDecimal(100);
    @Autowired
    private StarlightBalanceRepository starlightBalanceRepository;
    @Autowired
    private GCoinBalanceRepository gCoinBalanceRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveHistoryDetails(GCoinPaymentOrder order) {
        //新增暴鸡币流水表
        GCoinBill gCoinBill = new GCoinBill();
        gCoinBill.setOrderId(order.getOrderId());
        gCoinBill.setUserId(order.getUserId());
        gCoinBill.setSubject(order.getSubject());
        gCoinBill.setBody(order.getBody() + "，当前暴鸡币余额：" + order.getBalance());
        gCoinBill.setSourceId(order.getSourceId());
        gCoinBill.setFlowNo(String.valueOf(snowFlake.nextId()));
        gCoinBill.setOrderType(OrderTypeEnum.GCOIN_PAYMENT_ORDER.getCode());
        gCoinBill.setTradeType(TransactionTypeEnum.EXPENDITURE.getCode());
        //根据业务订单类型填写订单维度流水
        String orderType = order.getOrderType();
        gCoinBill.setOrderDimension(getOrderDimensionalityCode(BizOrderType.valueOf(Integer.valueOf(orderType))));

        gCoinBill.setAmount(order.getAmount());
        gCoinBill = gCoinBillRepository.save(gCoinBill);

        return saveSuccess(gCoinBill);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveHistoryDetails(GCoinRechargeOrder order) {
        GCoinBill gCoinBill = new GCoinBill();
        gCoinBill.setOrderId(order.getOrderId());
        gCoinBill.setUserId(order.getUserId());
        gCoinBill.setSubject(order.getSubject());
        gCoinBill.setBody(order.getBody() + "，当前暴击值余额：" + order.getBalance());
        gCoinBill.setBody(order.getBody() + "，当前暴击值余额：");
        gCoinBill.setSourceId(order.getSourceId());
        gCoinBill.setFlowNo(String.valueOf(snowFlake.nextId()));
        gCoinBill.setAmount(order.getGcoinAmount());
        gCoinBill.setOrderType(OrderTypeEnum.RECHARGE_ORDER.getCode());
        gCoinBill.setTradeType(TransactionTypeEnum.INCOME.getCode());
        //订单维度：支付系统-暴击值充值
        gCoinBill.setOrderDimension(OrderDimensionalityEnum.GCOIN_RECHARGE.getCode());
        gCoinBill.setChannel(order.getChannel());
        gCoinBill = gCoinBillRepository.save(gCoinBill);

        return saveSuccess(gCoinBill);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveHistoryDetails(DeductOrder order) {
        if (DeductCurrencyTypeEnum.GCOIN.getCode().equals(order.getCurrencyType())) {
            GCoinBill gCoinBill = new GCoinBill();
            gCoinBill.setOrderId(order.getOrderId());
            gCoinBill.setUserId(order.getUserId());
            gCoinBill.setAmount(order.getAmount());
            gCoinBill.setSubject(order.getSubject());
            gCoinBill.setBody(order.getBody() + "，当前暴鸡币余额：" + order.getBalance());
            gCoinBill.setSourceId(SourceType.PLATFORM.getCode());
            gCoinBill.setFlowNo(String.valueOf(snowFlake.nextId()));

            gCoinBill.setOrderType(OrderTypeEnum.GCOIN_WITHDRAW_ORDER.getCode());
            gCoinBill.setTradeType(TransactionTypeEnum.EXPENDITURE.getCode());
            String orderType = order.getOrderType();
            //订单维度：支付系统-暴鸡币扣除
            gCoinBill.setOrderDimension(OrderDimensionalityEnum.GCOIN_DEDUCT.getCode());
            gCoinBill.setChannel(PayChannelEnum.PLATFORM_SYSTEM.getValue());
            gCoinBill = gCoinBillRepository.save(gCoinBill);

            return saveSuccess(gCoinBill);
        } else if (DeductCurrencyTypeEnum.STARLIGHT.getCode().equals(order.getCurrencyType())) {
            StarlightBill starlightBill = new StarlightBill();
            starlightBill.setOrderId(order.getOrderId());
            starlightBill.setUserId(order.getUserId());
            starlightBill.setAmount(order.getAmount());
            starlightBill.setSubject(order.getSubject());
            starlightBill.setBody(order.getBody() + "，当前暴击值余额：" + order.getBalance());
            starlightBill.setSourceId(SourceType.PLATFORM.getCode());
            starlightBill.setFlowNo(String.valueOf(snowFlake.nextId()));

            starlightBill.setOrderType(OrderTypeEnum.STAR_WITHDRAW_ORDER.getCode());
            starlightBill.setTradeType(TransactionTypeEnum.EXPENDITURE.getCode());
            //订单维度：支付系统-暴击值扣除
            starlightBill.setOrderDimension(OrderDimensionalityEnum.STARLIGHT_DEDUCT.getCode());
            starlightBill.setChannel(PayChannelEnum.PLATFORM_SYSTEM.getValue());
            starlightBill = starlightBillRepository.save(starlightBill);

            return saveSuccess(starlightBill);
        } else {
            logger.warn("未定义的扣款货币类型，扣款订单流水保存失败！");
            throw new BusinessException(BizExceptionEnum.CURRENCY_TYPE_EMPTY);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveHistoryDetails(GCoinRefundOrder order) {
        //新增“暴鸡币”流水表
        GCoinBill gCoinBill = new GCoinBill();
        gCoinBill.setOrderId(order.getOrderId());
        gCoinBill.setUserId(order.getUserId());
        gCoinBill.setSubject(order.getSubject());
        gCoinBill.setBody(order.getBody() + "，当前暴鸡币余额：" + order.getBalance());
        gCoinBill.setSourceId(order.getSourceId());
        gCoinBill.setFlowNo(String.valueOf(snowFlake.nextId()));
        gCoinBill.setAmount(order.getAmount());
        gCoinBill.setOrderType(OrderTypeEnum.REFUND_ORDER.getCode());
        gCoinBill.setTradeType(TransactionTypeEnum.REFUND.getCode());
        String orderType = order.getOrderType();
        gCoinBill.setOrderDimension(this.getOrderDimensionalityCode(BizOrderType.valueOf(orderType)));
        gCoinBill = gCoinBillRepository.save(gCoinBill);

        return saveSuccess(gCoinBill);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveHistoryDetails(GCoinRewardOrder order) {
        //新增暴鸡币流水表
        GCoinBill gCoinBill = new GCoinBill();
        gCoinBill.setOrderId(order.getOrderId());
        gCoinBill.setUserId(order.getUserId());
        gCoinBill.setSubject(order.getSubject());
        gCoinBill.setBody(order.getBody() + "，当前暴鸡币余额：" + order.getBalance());
        gCoinBill.setSourceId(order.getSourceId());
        gCoinBill.setFlowNo(String.valueOf(snowFlake.nextId()));
        gCoinBill.setUserId(order.getUserId());
        gCoinBill.setAmount(order.getGcoinAmount());
        gCoinBill.setOrderType(OrderTypeEnum.GCOIN_REWARD_ORDER.getCode());
        gCoinBill.setTradeType(TransactionTypeEnum.EXPENDITURE.getCode());
        gCoinBill.setChannel(PayChannelEnum.WALLET_PAY.getValue());
        //订单维度：支付后台-打赏订单
        gCoinBill.setOrderDimension(OrderDimensionalityEnum.REWARD_ORDER.getCode());
        gCoinBill = gCoinBillRepository.save(gCoinBill);
        //新增“暴击值”流水表
        StarlightBill starlightBill = new StarlightBill();
        starlightBill.setOrderId(order.getOrderId());
        starlightBill.setUserId(order.getUserId());
        starlightBill.setSubject(order.getSubject());
        starlightBill.setBody(order.getBody() + "，当前暴击值余额：" + order.getStartLightBalance());
        starlightBill.setSourceId(order.getSourceId());
        starlightBill.setFlowNo(String.valueOf(snowFlake.nextId()));
        starlightBill.setUserId(order.getReceivedUserId());
        starlightBill.setAmount(order.getStarlightAmount());
        starlightBill.setOrderType(OrderTypeEnum.GCOIN_REWARD_ORDER.getCode());
        starlightBill.setTradeType(TransactionTypeEnum.INCOME.getCode());
        starlightBill.setChannel(PayChannelEnum.WALLET_PAY.getValue());
        starlightBill.setOrderDimension(OrderDimensionalityEnum.REWARD_ORDER.getCode());
        starlightBill = starlightBillRepository.save(starlightBill);

        return saveSuccess(gCoinBill) && saveSuccess(starlightBill);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveHistoryDetails(OrderIncome order) {
        BigDecimal total = new BigDecimal(order.getTotalAmount()).divide(new BigDecimal(100));
        BigDecimal platIncome = total.multiply(order.getRatio()).setScale(2, BigDecimal.ROUND_DOWN);
        BigDecimal baojiIncome = total.subtract(platIncome);
        StarlightBill starlightBill = new StarlightBill();
        starlightBill.setFlowNo(String.valueOf(snowFlake.nextId()));
        starlightBill
                .setSubject("订单收益-" + BizOrderType.valueOf(order.getOrderType()).getMsg());
        StringBuffer bodySB = new StringBuffer();
        bodySB.append("订单收益-")
                .append(BizOrderType.valueOf(order.getOrderType()).getMsg())
                .append("，支付金额：")
                .append(total)
                .append("个暴鸡币，实际收入：")
                .append(baojiIncome.floatValue())
                .append("个暴击值，平台抽成：")
                .append(platIncome.floatValue())
                .append("个暴击值。")
                .append("当前暴击值余额：" + order.getBalance());
        starlightBill.setBody(bodySB.toString());
        starlightBill.setUserId(order.getUserId());
        //结算订单类型
        starlightBill.setOrderType(OrderTypeEnum.SETTLEMENT_ORDER.getCode());
        //交易收入类型
        starlightBill.setTradeType(TransactionTypeEnum.INCOME.getCode());
        //订单类别-订单类型映射
        int orderType = order.getOrderType();
        starlightBill.setOrderDimension(getOrderDimensionalityCode(BizOrderType.valueOf(orderType)));
        starlightBill.setChannel(PayChannelEnum.PLATFORM_SYSTEM.getValue());
        starlightBill.setSourceId(SourceType.PLATFORM.getCode());
        starlightBill.setOrderId(order.getIncomeOrdernum());
        starlightBill.setAmount(baojiIncome);
        starlightBill = starlightBillRepository.save(starlightBill);

        return saveSuccess(starlightBill);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveHistoryDetails(WithdrawOrder order) {
        StarlightBill starlightBill = new StarlightBill();
        starlightBill.setOrderId(order.getOrderId());
        starlightBill.setUserId(order.getUserId());
        starlightBill.setSubject(order.getSubject());
        starlightBill.setBody(order.getBody() + "，当前暴击值余额：" + order.getBalance());
        starlightBill.setSourceId(order.getSourceId());
        starlightBill.setFlowNo(String.valueOf(snowFlake.nextId()));
        starlightBill.setAmount(order.getAmount());
        starlightBill.setChannel(order.getChannel());

        boolean saveGCoinBillSuccess = true;

        String orderType = order.getOrderType();
        if (OrderTypeEnum.REFUND_ORDER.getCode().equals(orderType)) {
            //提现失败-订单维度为暴击值提现
            starlightBill.setOrderType(OrderTypeEnum.REFUND_ORDER.getCode());
            starlightBill.setTradeType(TransactionTypeEnum.REFUND.getCode());
            starlightBill.setOrderDimension(OrderDimensionalityEnum.WITHDRAW_ORDER.getCode());
        } else if (OrderTypeEnum.SCORE_EXCHANGE_ORDER.getCode().equals(orderType)) {
            //鸡分兑换
            //订单维度：积分兑换暴击值
            starlightBill.setOrderDimension(OrderDimensionalityEnum.SCORE_EXCHANGE_STARLIGHT.getCode());
            starlightBill.setOrderType(OrderTypeEnum.SCORE_EXCHANGE_ORDER.getCode());
            starlightBill.setTradeType(TransactionTypeEnum.INCOME.getCode());
        } else if (OrderTypeEnum.STAR_EXCHANGE_ORDER.getCode().equals(orderType)) {
            //暴击值兑换暴鸡币
            //订单维度：暴击值兑换暴鸡币
            starlightBill.setOrderDimension(OrderDimensionalityEnum.STARLIGHT_EXCHANGE_GCOIN.getCode());
            starlightBill.setOrderType(OrderTypeEnum.STAR_EXCHANGE_ORDER.getCode());
            starlightBill.setTradeType(TransactionTypeEnum.EXPENDITURE.getCode());
            //新增暴鸡币流水表
            GCoinBill gCoinBill = new GCoinBill();
            gCoinBill.setOrderId(order.getOrderId());
            gCoinBill.setUserId(order.getUserId());
            gCoinBill.setSubject(order.getSubject());
            gCoinBill.setBody(order.getBody() + "，当前暴鸡币余额：" + order.getgCoinBalance());
            gCoinBill.setSourceId(order.getSourceId());
            gCoinBill.setFlowNo(String.valueOf(snowFlake.nextId()));
            gCoinBill.setUserId(order.getUserId());
            gCoinBill.setAmount(order.getAmount());
            gCoinBill.setOrderType(OrderTypeEnum.STAR_EXCHANGE_ORDER.getCode());
            //交易收入类型
            gCoinBill.setTradeType(TransactionTypeEnum.INCOME.getCode());
            gCoinBill.setChannel(order.getChannel());
            //订单维度
            gCoinBill.setOrderDimension(OrderDimensionalityEnum.STARLIGHT_EXCHANGE_GCOIN.getCode());
            gCoinBill = gCoinBillRepository.save(gCoinBill);

            saveGCoinBillSuccess = saveSuccess(gCoinBill);

        } else {
            //订单维度：暴击值提现
            starlightBill.setOrderType(OrderTypeEnum.WITHDRAW_ORDER.getCode());
            starlightBill.setTradeType(TransactionTypeEnum.EXPENDITURE.getCode());
            starlightBill.setOrderDimension(OrderDimensionalityEnum.WITHDRAW_ORDER.getCode());
        }
        //新增“暴击值”流水表
        starlightBill = starlightBillRepository.save(starlightBill);

        return saveSuccess(starlightBill) && saveGCoinBillSuccess;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WalletsVO getBalance(final String userId) {
        try {
            WalletsVO walletsVO = new WalletsVO();

            //获取暴鸡币
            GCoinBalance gCoinBalance = gCoinBalanceRepository.findOneByUserId(userId);
            if (gCoinBalance == null) {
                //新增暴鸡币表
                gCoinBalance = AccountUtil.generateGcoinBalance(userId);
                gCoinBalanceRepository.save(gCoinBalance);
                walletsVO.setGcoinAmount(NUM_ZERO);
            } else {
                walletsVO.setGcoinAmount(gCoinBalance.getUsableAmount().multiply(NUM_100));
            }

            // 获取暴击值
            StarlightBalance starlightBalance = starlightBalanceRepository.findOneByUserId(userId);
            if (starlightBalance == null) {
                //新增“暴击值”表
                starlightBalance = AccountUtil.generateStarlightBalance(userId);
                starlightBalanceRepository.save(starlightBalance);
                walletsVO.setStarlightAmount(NUM_ZERO);
            } else {
                walletsVO.setStarlightAmount(starlightBalance.getUsableAmount().multiply(NUM_100));
            }

            return walletsVO;
        } catch (Exception e) {
            logger.debug("打印用户余额查询服务接口异常：" + e.getMessage(), e);
            throw new BusinessException(BizExceptionEnum.USER_WALLETS_SELECT);
        }
    }

    private boolean saveSuccess(AbstractEntity entity) {
        return (entity != null && entity.getId() != null && entity.getId().intValue() != 0);
    }

    private String getOrderDimensionalityCode(BizOrderType orderType) {
        switch (orderType) {
            case GAME_ORDER:
                return OrderDimensionalityEnum.GAME_ORDER.getCode();
            case CERTIFIED_ORDER:
                return OrderDimensionalityEnum.CERTIFIED_ORDER.getCode();
            case RECHARGE_ORDER:
                return OrderDimensionalityEnum.RECHARGE_ORDER.getCode();
            case BONUS_ORDER:
                return OrderDimensionalityEnum.BONUS_ORDER.getCode();
            case ILLEGAL_CHARGE:
                return OrderDimensionalityEnum.ILLEGAL_CHARGE.getCode();
            case BALANCE_WELFARE_ORDER:
                return OrderDimensionalityEnum.BALANCE_WELFARE_ORDER.getCode();
            case ACTIVITY_BOUNTY:
                return OrderDimensionalityEnum.ACTIVITY_BOUNTY.getCode();
            case FLEET:
                return OrderDimensionalityEnum.FLEET.getCode();
            case OFFER_A_REWARD:
                return OrderDimensionalityEnum.OFFER_A_REWARD.getCode();
            case SYS_RECHARGE_ORDER:
                return OrderDimensionalityEnum.SYS_RECHARGE_ORDER.getCode();
            case WITHDRAW:
                return OrderDimensionalityEnum.WITHDRAW.getCode();
            case SYS_DEDUCT_ORDER:
                return OrderDimensionalityEnum.SYS_DEDUCT_ORDER.getCode();
            case STUDIO_TRANSFER_ORDER:
                return OrderDimensionalityEnum.STUDIO_TRANSFER_ORDER.getCode();
            case WEEKLY_LUCKY_ACTIVITY_ORDER:
                return OrderDimensionalityEnum.WEEKLY_LUCKY_ACTIVITY_ORDER.getCode();
            case SKILL:
                return OrderDimensionalityEnum.SKILL.getCode();
            case DNF_MINI_PROGRAM_ORDER:
                return OrderDimensionalityEnum.DNF_MINI_PROGRAM_ORDER.getCode();
            case RPG_CERTIFIED_ORDER:
                return OrderDimensionalityEnum.RPG_CERTIFIED_ORDER.getCode();
            case GCOIN_RECHARGE_ORDER:
                return OrderDimensionalityEnum.GCOIN_RECHARGE_ORDER.getCode();
            case STARLIGHT_CONVERT_ORDER:
                return OrderDimensionalityEnum.STARLIGHT_CONVERT_ORDER.getCode();
            default:
                return null;

        }
    }
}

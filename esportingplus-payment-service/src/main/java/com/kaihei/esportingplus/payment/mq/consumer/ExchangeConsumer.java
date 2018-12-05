package com.kaihei.esportingplus.payment.mq.consumer;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.payment.api.enums.RedisKeyType;
import com.kaihei.esportingplus.payment.api.enums.WithdrawStatusType;
import com.kaihei.esportingplus.payment.api.vo.ExchangeUpdateMessageVO;
import com.kaihei.esportingplus.payment.data.jpa.repository.GCoinBalanceRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.StarlightBalanceRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.WithdrawOrderRepository;
import com.kaihei.esportingplus.payment.domain.entity.GCoinBalance;
import com.kaihei.esportingplus.payment.domain.entity.StarlightBalance;
import com.kaihei.esportingplus.payment.domain.entity.WithdrawOrder;
import com.kaihei.esportingplus.payment.service.BillFlowService;
import com.kaihei.esportingplus.payment.service.WithdrawService;
import com.maihaoche.starter.mq.annotation.MQConsumer;
import com.maihaoche.starter.mq.base.AbstractMQPushConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 兑换消费者
 *
 * @author chenzhenjun
 **/
@Service
@MQConsumer(topic = RocketMQConstant.PAYMENT_TOPIC, tag = RocketMQConstant.STARLIGHT_EXCHANGE_TAG, consumerGroup = RocketMQConstant.STARLIGHT_EXCHANGE_CONSUMER_GROUP)
public class ExchangeConsumer extends AbstractMQPushConsumer<ExchangeUpdateMessageVO> {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeConsumer.class);
    private static final CacheManager cacheManager = CacheManagerFactory.create();

    @Autowired
    private WithdrawOrderRepository withdrawOrderRepository;

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private StarlightBalanceRepository starlightBillRepository;

    @Autowired
    private BillFlowService paymentBillService;

    @Autowired
    private GCoinBalanceRepository gCoinBalanceRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean process(ExchangeUpdateMessageVO message, Map<String, Object> extMap) {
        logger.debug("ExchangeConsumer >> process >> message >> " + message);
        logger.debug("ExchangeConsumer >> process >> extMap >> " + extMap);
        try {
            //暴击值兑换
            this.executeExchange(message);

        } catch (BusinessException e) {
            logger.error("ExchangeConsumer >> process >> BusinessException >> " + e.getErrMsg());
            if (BizExceptionEnum.COMSUMER_LOCK.getErrCode() == e.getErrCode()) {
                return true;
            }
            throw new BusinessException(BizExceptionEnum.ROCKETMQ_COMSUMER_ERROR);
        } catch (Exception e) {
            logger.error("process >> exception >> " + e.getMessage());
            logger.error("系统异常");
        }
        return true;
    }

    /**
     * 执行扣减暴击值，新增暴鸡币操作 生成流水
     */
    @Transactional(rollbackFor = Exception.class)
    public void executeExchange(ExchangeUpdateMessageVO message) throws Exception {
        ResponsePacket result = null;
        String userId = message.getUserId();
        String orderId = message.getOrderId();
        String consumerLock = "exchange:consume:lock:" + orderId;
        WithdrawOrder withdrawOrder = null;
        GCoinBalance gCoinBalance = null;
        StarlightBalance balanceEntity = null;
        try {
            //判断这个订单是否正在被消费
            boolean hasLock = cacheManager.exists(consumerLock);
            if (!hasLock) {
                //设置锁失效时间为3秒
                cacheManager.set(consumerLock, "lock", 3);
            } else {
                return;
            }

            withdrawOrder = withdrawService.getExchangeOrderInfo(orderId, userId);
            if (withdrawOrder == null) {
                logger.error("consume >> executeExchange >> exception : "
                        + BizExceptionEnum.ORDER_NOT_EXIST.getErrMsg());
                throw new BusinessException(BizExceptionEnum.ORDER_NOT_EXIST);
            }

            //如果消息已经处理完成则提前结束流程
            if (!WithdrawStatusType.CREATE.getCode().equals(withdrawOrder.getState())) {
                logger.error("consume >> executeExchange >> Exception "
                        + BizExceptionEnum.EXCHANGE_FINISH.getErrMsg());
                throw new BusinessException(BizExceptionEnum.EXCHANGE_FINISH);
            }

            gCoinBalance = gCoinBalanceRepository.findOneByUserId(userId);
            if (gCoinBalance == null) {
                logger.error("consume >> executeExchange >> exception : "
                        + BizExceptionEnum.GCOINCCOUNT_NOT_EXIST.getErrMsg());
                throw new BusinessException(BizExceptionEnum.GCOINCCOUNT_NOT_EXIST);
            }

            // 暴击值余额账户
            balanceEntity = starlightBillRepository.findOneByUserId(userId);

            withdrawOrder.setState(WithdrawStatusType.SUCCESS.getCode());
            withdrawOrderRepository.save(withdrawOrder);
            // 余额更新
            BigDecimal withdrawAmount = withdrawOrder.getAmount(); //提现金额
            BigDecimal frozenAmount = balanceEntity.getFrozenAmount();
            BigDecimal frozenAmountAfter = frozenAmount.subtract(withdrawAmount); // 冻结的额度恢复
            balanceEntity.setFrozenAmount(frozenAmountAfter);
            starlightBillRepository.save(balanceEntity);

            //暴鸡币账户 增加
            BigDecimal useableAmount = gCoinBalance.getUsableAmount();
            BigDecimal totalBalance = gCoinBalance.getGcoinBalance();
            BigDecimal useableAmountAfter = useableAmount.add(withdrawAmount);
            BigDecimal totalAmountAfter = totalBalance.add(withdrawAmount);
            gCoinBalance.setUsableAmount(useableAmountAfter);
            gCoinBalance.setGcoinBalance(totalAmountAfter);
            gCoinBalanceRepository.save(gCoinBalance);

            withdrawOrder.setBalance(balanceEntity.getUsableAmount().toString());
            withdrawOrder.setgCoinBalance(
                    gCoinBalance.getUsableAmount().setScale(2, BigDecimal.ROUND_DOWN).toString());
            paymentBillService.saveRecord(withdrawOrder);

            String key = RedisKeyType.EXCHANGE.getCode() + ":" + userId + ":" + orderId;
            cacheManager.del(key);

            cacheManager.set(key, withdrawOrder, 30 * 60);

            result = ResponsePacket.onSuccess();

        } catch (BusinessException e) {
            logger.error("executeExchange >> Exception : " + e.getErrMsg());
            result = ResponsePacket.onError(e.getErrCode(), e.getErrMsg());

            withdrawOrder.setState(WithdrawStatusType.FAIL.getCode());
            withdrawOrderRepository.save(withdrawOrder);

            BigDecimal withdrawAmount = withdrawOrder.getAmount(); //提现金额
            BigDecimal frozenAmount = balanceEntity.getFrozenAmount();
            BigDecimal useableAmount = balanceEntity.getFrozenAmount(); // 可用暴击值
            BigDecimal frozenAmountAfter = frozenAmount.subtract(withdrawAmount); // 冻结的额度恢复
            BigDecimal useableAmountAfter = useableAmount.add(withdrawAmount); // 可用暴击值恢复
            balanceEntity.setBalance(useableAmountAfter);
            balanceEntity.setUsableAmount(useableAmountAfter);
            balanceEntity.setFrozenAmount(frozenAmountAfter);
            starlightBillRepository.save(balanceEntity);

        } finally {
            cacheManager.del(consumerLock);
        }

        logger.debug("executeExchange >> 出参 >> " + result);

    }
}

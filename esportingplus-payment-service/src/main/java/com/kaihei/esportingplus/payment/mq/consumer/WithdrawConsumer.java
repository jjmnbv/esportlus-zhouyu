package com.kaihei.esportingplus.payment.mq.consumer;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.HttpUtils;
import com.kaihei.esportingplus.payment.api.enums.RedisKeyType;
import com.kaihei.esportingplus.payment.api.enums.WithdrawNotifyEnum;
import com.kaihei.esportingplus.payment.api.enums.WithdrawStatusType;
import com.kaihei.esportingplus.payment.api.vo.WithdrawUpdateMessageVO;
import com.kaihei.esportingplus.payment.data.jpa.repository.StarlightBalanceRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.WithdrawOrderRepository;
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
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 提现消费者
 *
 * @author chenzhenjun
 **/
@Service
@MQConsumer(topic = RocketMQConstant.PAYMENT_TOPIC, tag=RocketMQConstant.STARLIGHT_WITHDRAW_TAG, consumerGroup = RocketMQConstant.STARLIGHT_WITHDRAW_CONSUMER_GROUP)
public class WithdrawConsumer extends AbstractMQPushConsumer<WithdrawUpdateMessageVO> {

    private static final Logger logger = LoggerFactory.getLogger(WithdrawConsumer.class);
    private static final CacheManager cacheManager = CacheManagerFactory.create();

    @Autowired
    private WithdrawOrderRepository withdrawOrderRepository;

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private StarlightBalanceRepository starlightBillRepository;

    @Autowired
    private BillFlowService billFlowService;

    @Override
    public boolean process(WithdrawUpdateMessageVO message, Map<String, Object> extMap) {
        logger.debug("WithdrawConsumer >> process >> message >> " + message);
        logger.debug("WithdrawConsumer >> process >> extMap >> " + extMap);
        try {
                //暴击值冻结
            this.executeWithdrawFrozen(message);

        } catch (BusinessException e) {
            logger.error("process >> BusinessException >> " + e.getErrMsg());
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
     * 执行冻结操作 回调python
     * @param message
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void executeWithdrawFrozen(WithdrawUpdateMessageVO message) throws Exception {
        ResponsePacket result = null;
        String userId = message.getUserId();
        String orderId = message.getOrderId();
        String notifyUrl = message.getNotifyUrl();
        String consumerLock = "withdraw:consume:lock:" + orderId;
        String notifyFlag = "";
        WithdrawOrder withdrawOrder = null;
        try {
            //判断这个订单是否正在被消费
            boolean hasLock = cacheManager.exists(consumerLock);
            if (!hasLock) {
                //设置锁失效时间为3秒
                cacheManager.set(consumerLock, "lock", 3);
            } else {
                logger.debug(">>executeWithdrawFrozen >> mq 已经被消费 orderId : {} ", orderId);
                return;
            }

            withdrawOrder = withdrawService.getWithdrawStateInfo(orderId,userId);
            if (withdrawOrder == null) {
                logger.error("consume >> executeWithdraw >> exception : " + BizExceptionEnum.ORDER_NOT_EXIST.getErrMsg());
                throw new BusinessException(BizExceptionEnum.ORDER_NOT_EXIST);
            }

            //如果消息已经处理完成则提前结束流程
            if (!WithdrawStatusType.CREATE.getCode().equals(withdrawOrder.getState())) {
                logger.error("consume >> executeWithdraw >> Exception " + BizExceptionEnum.WITHDRAW_FINISH.getErrMsg());
                throw new BusinessException(BizExceptionEnum.WITHDRAW_FINISH);
            }

            StarlightBalance balanceEntity = starlightBillRepository.findOneByUserId(userId);
            notifyFlag = withdrawOrder.getIsNotify(); // 普通暴鸡冻结
            BigDecimal withdrawMoney = withdrawOrder.getAmount();
            if (notifyFlag.equals(WithdrawNotifyEnum.NEED.getCode())) {
                // 冻结提现暴击值
                BigDecimal frozenMoney = balanceEntity.getFrozenAmount().add(withdrawMoney);
                balanceEntity.setFrozenAmount(frozenMoney); // 冻结 提现暴击值
                BigDecimal leftAmount = balanceEntity.getBalance().subtract(withdrawMoney);
                balanceEntity.setUsableAmount(leftAmount);
                balanceEntity.setBalance(leftAmount); // 总的也对应减掉
                starlightBillRepository.save(balanceEntity);

                withdrawOrder.setBalance(
                        balanceEntity.getUsableAmount().setScale(2, BigDecimal.ROUND_DOWN)
                                .toString());
                billFlowService.saveRecord(withdrawOrder);

            } else {  // 工作室直接扣减

                withdrawOrder.setState(WithdrawStatusType.SUCCESS.getCode());
                withdrawOrderRepository.save(withdrawOrder);

                BigDecimal leftAmount = balanceEntity.getBalance().subtract(withdrawMoney);
                balanceEntity.setUsableAmount(leftAmount);
                balanceEntity.setBalance(leftAmount); // 总的也对应减掉
                starlightBillRepository.save(balanceEntity);

                withdrawOrder.setBalance(
                        balanceEntity.getUsableAmount().setScale(2, BigDecimal.ROUND_DOWN)
                                .toString());
                billFlowService.saveRecord(withdrawOrder);

                // 更新redis, 变更 过期时间
                String key = RedisKeyType.WITHDRAW.getCode() + ":" + withdrawOrder.getUserId() + ":" + withdrawOrder
                        .getOrderId();
                cacheManager.del(key);

                cacheManager.set(key, withdrawOrder, 30 * 60);

            }

            result = ResponsePacket.onSuccess();

        } catch (BusinessException e) {
            logger.error("executeWithdraw >> Exception : " + e.getErrMsg());
            result = ResponsePacket.onError(e.getErrCode(), e.getErrMsg());
            result.setData(withdrawOrder);
        } finally {
            RestTemplate restTemplate = new RestTemplate();
            result.setData(withdrawOrder);
            // 返回给python的也要是分
            BigDecimal amountYuan = withdrawOrder.getAmount();
            withdrawOrder.setAmount(amountYuan.multiply(new BigDecimal("100")));
            logger.debug("consumer >> executeWithdraw >> python入参 >> " + result.toString());
            ResponsePacket pythonResponse = restTemplate.postForObject(notifyUrl, HttpUtils.buildParam(result), ResponsePacket.class);
            logger.debug("pythonResponse >> " + pythonResponse);

            cacheManager.del(consumerLock);
        }
    }
}

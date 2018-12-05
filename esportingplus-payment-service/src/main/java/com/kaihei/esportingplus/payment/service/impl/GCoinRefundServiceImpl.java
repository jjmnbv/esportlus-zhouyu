package com.kaihei.esportingplus.payment.service.impl;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.algorithm.SnowFlake;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.ApplePayException;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.DateUtil;
import com.kaihei.esportingplus.payment.api.enums.GCoinPaymentStateType;
import com.kaihei.esportingplus.payment.api.enums.GCoinRefundStateType;
import com.kaihei.esportingplus.payment.api.params.GCoinRefundParams;
import com.kaihei.esportingplus.payment.api.vo.GCoinRefundPreVo;
import com.kaihei.esportingplus.payment.api.vo.GCoinRefundVo;
import com.kaihei.esportingplus.payment.data.jpa.repository.GCoinBalanceRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.GCoinPaymentRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.GCoinRefundRepository;
import com.kaihei.esportingplus.payment.domain.entity.GCoinBalance;
import com.kaihei.esportingplus.payment.domain.entity.GCoinPaymentOrder;
import com.kaihei.esportingplus.payment.domain.entity.GCoinRefundOrder;
import com.kaihei.esportingplus.payment.mq.message.MQSimpleOrder;
import com.kaihei.esportingplus.payment.mq.producer.GCoinRefundTransactionProducer;
import com.kaihei.esportingplus.payment.service.BillFlowService;
import com.kaihei.esportingplus.payment.service.GCoinRefundService;
import com.kaihei.esportingplus.payment.util.AccountUtil;
import com.kaihei.esportingplus.payment.util.CommonUtils;
import com.kaihei.esportingplus.payment.util.ExternalPaymentUtil;
import com.maihaoche.starter.mq.base.MessageBuilder;
import org.apache.rocketmq.common.message.Message;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 暴鸡币退款服务实现
 *
 * @author xusisi
 * @create 2018-09-27 11:21
 **/
@Service
public class GCoinRefundServiceImpl implements GCoinRefundService {

    private static Logger logger = LoggerFactory.getLogger(GCoinRefundService.class);

    @Autowired
    private GCoinPaymentRepository gcoinPaymentRepository;

    @Autowired
    private GCoinRefundRepository gcoinRefundRepository;

    private static final CacheManager cacheManager = CacheManagerFactory.create();

    @Autowired
    private GCoinRefundTransactionProducer gcoinRefundTransactionProducer;

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private GCoinBalanceRepository gCoinBalanceRepository;

    @Autowired
    private BillFlowService billFlowService;

    @Autowired
    private ExternalPaymentUtil externalPaymentUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GCoinRefundPreVo createRefund(GCoinRefundParams gcoinRefundParams) throws BusinessException {
        logger.debug("入参 >> GCoinRefundParams : {} ", gcoinRefundParams);
        String outTradeNo = gcoinRefundParams.getOutTradeNo();
        String orderType = gcoinRefundParams.getOrderType();
        String outRefundNo = gcoinRefundParams.getOutRefundNo();
        String userId = gcoinRefundParams.getUserId();
        String refundAmountTemp = gcoinRefundParams.getAmount();

        Integer intRefundAmountTemp = CommonUtils.strToInteger(refundAmountTemp);

        //传过来的金额是分，数据库保存的是元
        BigDecimal refundAmount = new BigDecimal(intRefundAmountTemp).divide(new BigDecimal(100));
        //校验是否存在支付订单
        logger.debug("orderType : {}, outTradeNo : {} , refundAmount : {} ", orderType, outTradeNo, refundAmount);
        GCoinPaymentOrder gcoinPayment = this.checkGcoinPaymentState(orderType, outTradeNo);
        logger.debug("GCoinPaymentOrder : {} ", gcoinPayment);

        //校验是否该退款已经提交过,如果已经提交，判断退款订单状态，是否是处理完，退款中会返回正在处理中的退款订单信息
        GCoinRefundOrder gCoinRefundOrder = gcoinRefundRepository.findOneByOrderTypeAndOutRefundNo(orderType, outRefundNo);
        logger.debug("gcoinRefundOrder :{} ", gCoinRefundOrder);
        if (gCoinRefundOrder != null) {
            String refundState = gCoinRefundOrder.getState();
            if (!GCoinRefundStateType.CREATE.getCode().equals(refundState)) {
                logger.error("exception : {} ", BizExceptionEnum.GCOIN_REFUND_ORDER_FINISHED.getErrMsg());
                throw new BusinessException(BizExceptionEnum.GCOIN_REFUND_ORDER_FINISHED);
            }
            GCoinRefundPreVo vo = new GCoinRefundPreVo();
            vo.setOrderId(gCoinRefundOrder.getOrderId());
            vo.setOrderType(Integer.valueOf(gCoinRefundOrder.getOrderType()));
            vo.setOutRefundNo(gCoinRefundOrder.getOutRefundNo());
            vo.setOutTradeNo(gCoinRefundOrder.getOutTradeNo());

            logger.debug("出参 >>  response : {}", vo);
            return vo;
        }

        String createRefundLock = String.format(RedisKey.CREATE_GCOIN_REFUND_LOCK_KEY, orderType, outRefundNo);
        RLock lock = cacheManager.redissonClient().getLock(createRefundLock);
        if (lock.isLocked()) {
            logger.warn("该订单已经处理 >> orderType : {} ,outRefundNo :{} ", orderType, outRefundNo);
            logger.error("exception : {} ", BizExceptionEnum.GCOIN_REFUND_ORDER_CREATING.getErrMsg());
            throw new BusinessException(BizExceptionEnum.GCOIN_REFUND_ORDER_CREATING);
        }

        try {
            lock.expire(3, TimeUnit.SECONDS);

            //要求退款金额是否满足可退金额
            //可退金额=订单总金额-已退款金额-退款中金额
            BigDecimal usableRefundAmount =
                    gcoinPayment.getAmount().subtract(gcoinPayment.getRefundedAmount()).subtract(gcoinPayment.getRefundingAmount());

            //要退金额大于可退金额
            if (refundAmount.compareTo(usableRefundAmount) > 0) {
                logger.error("createRefund >> exception : {} ", BizExceptionEnum.GCOIN_REFUND_PAYMENT_ORDER_AMOUNT_NOT_ENOUGH.getErrMsg());
                throw new BusinessException(BizExceptionEnum.GCOIN_REFUND_PAYMENT_ORDER_AMOUNT_NOT_ENOUGH);
            }

            String flowNo = String.valueOf(snowFlake.nextId());

            //将订单ID发送至MQ
            try {
                MQSimpleOrder mqSimpleOrder = new MQSimpleOrder();
                String notifyUrl = gcoinRefundParams.getNotifyUrl();
                mqSimpleOrder.setNotifyUrl(notifyUrl);
                mqSimpleOrder.setOrderId(flowNo);
                mqSimpleOrder.setUserId(userId);
                mqSimpleOrder.setOutTradeNo(outRefundNo);
                gcoinRefundParams.setOrderId(flowNo);

                Message message =
                        MessageBuilder.of(mqSimpleOrder).topic(RocketMQConstant.PAYMENT_TOPIC).tag(RocketMQConstant.GCOIN_REFUND_TAG).build();

                message.setTransactionId(
                        CommonUtils.genTransactionId(RocketMQConstant.PAYMENT_TRANSACTION_ID, flowNo));

                logger.debug("sendMessage >> start >> topic :{} , tag :{} , orderId : {} ,gcoinRefundParams :{}",
                        RocketMQConstant.PAYMENT_TOPIC,
                        RocketMQConstant.GCOIN_REFUND_TAG,
                        flowNo,
                        gcoinRefundParams);

                gcoinRefundTransactionProducer.sendMessageInTransaction(message, gcoinRefundParams);

                logger.debug("sendMessage >> end >> topic :{} , tag :{} , orderId : {} , gcoinRefundParams : {} ",
                        RocketMQConstant.PAYMENT_TOPIC,
                        RocketMQConstant.GCOIN_REFUND_TAG,
                        flowNo,
                        gcoinRefundParams);

            } catch (Exception e) {
                logger.error("exception : " + e.getMessage());
                throw new BusinessException(BizExceptionEnum.ROCKETMQ_PRODUCER_ERROR);
            }

            GCoinRefundPreVo vo = new GCoinRefundPreVo();
            vo.setOrderId(flowNo);
            vo.setOrderType(Integer.valueOf(orderType));
            vo.setOutRefundNo(outRefundNo);
            vo.setOutTradeNo(outTradeNo);

            logger.debug("createRefund >> end >> response : {}", flowNo);
            return vo;
        } catch (BusinessException e) {
            throw e;
        } finally {

            //释放分布式锁
            try {
                if (lock != null && lock.isLocked()) {
                    lock.unlock();
                }
            } catch (Exception e) {
                logger.error("分布式锁解锁异常");
            }

        }

    }

    @Override
    public GCoinRefundVo getRefundVo(String orderId, String orderType, String outRefundNo) {
        GCoinRefundOrder gcoinRefund = null;
        GCoinRefundVo vo = null;
        Boolean flag1 = true;
        if (orderId != null && !"".equals(orderId.trim())) {
            gcoinRefund = gcoinRefundRepository.findOneByOrderId(orderId);
        } else {
            flag1 = false;
        }
        if (gcoinRefund != null) {
            vo = exchangeToVo(gcoinRefund);
            return vo;
        }

        Boolean flag2 = orderType != null && !"".equals(orderType.trim());
        Boolean flag3 = outRefundNo != null && !"".equals(outRefundNo.trim());

        Boolean flag = flag1 || (flag2 && flag3);

        //有一个不成立时
        if (!flag) {
            logger.error("getVo >> exception :{} ", BizExceptionEnum.GCOIN_REFUND_SEARCH_PARAM_ERROR.getErrMsg());
            throw new BusinessException(BizExceptionEnum.GCOIN_REFUND_SEARCH_PARAM_ERROR);
        }

        gcoinRefund = gcoinRefundRepository.findOneByOrderTypeAndOutRefundNo(orderType, outRefundNo);

        if (gcoinRefund == null) {
            return null;
        }
        vo = exchangeToVo(gcoinRefund);
        return vo;
    }

    @Override
    public GCoinRefundVo getRefundVo(String orderId) {
        GCoinRefundOrder gcoinRefund = gcoinRefundRepository.findOneByOrderId(orderId);
        if (gcoinRefund == null) {
            return null;
        }
        String orderType = gcoinRefund.getOrderType();
        String outRefundNo = gcoinRefund.getOutRefundNo();
        return this.getRefundVo(orderId, orderType, outRefundNo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refreshRefundOrder(String orderId, String userId) {

        logger.debug("refreshRefundOrder >> start >> orderId : {}, userId : {} ", orderId, userId);

        GCoinRefundOrder refundOrder = gcoinRefundRepository.findOneByOrderIdAndUserId(orderId, userId);

        logger.debug("refreshRefundOrder >> refundOrder {} :", refundOrder);

        if (refundOrder == null) {
            logger.warn("refreshRefundOrder >> exception : {}", BizExceptionEnum.GCOIN_REFUND_ORDER_NOT_EXIST.getErrMsg());
            // throw new BusinessException(BizExceptionEnum.GCOIN_REFUND_ORDER_NOT_EXIST);
            return;
        }

        String stateCode = refundOrder.getState();
        if (!GCoinRefundStateType.CREATE.getCode().equals(stateCode)) {
            logger.debug("refreshRefundOrder >> end >> orderId : {}, 该订单退款流程结束，无需再刷新状态", orderId);
            return;
        }
        refundOrder.setState(GCoinRefundStateType.FAILED.getCode());
        gcoinRefundRepository.save(refundOrder);

        //更新支付订单中的退款金额信息
        GCoinPaymentOrder gCoinPaymentOrder = gcoinPaymentRepository.findOneByOrderIdAndUserId(orderId, userId);
        //旧的退款中的金额
        BigDecimal refundingAmount = gCoinPaymentOrder.getRefundingAmount();
        //退款订单金额
        BigDecimal refundAmount = refundOrder.getAmount();
        //新的退款金额 = 旧的退款中金额-订单退款金额
        gCoinPaymentOrder.setRefundingAmount(refundingAmount.subtract(refundAmount));
        gcoinPaymentRepository.save(gCoinPaymentOrder);

        logger.debug("refreshOrderInfo >> end >> orderId :{} 该订单因超时未处理，状态变更为退款失败。", orderId);
    }

    @Override
    public GCoinPaymentOrder checkGcoinPaymentState(String orderType, String outTradeNo) {
        logger.debug("checkGcoinPaymentState >> orderType : {},outTradeNo : {} ", orderType, outTradeNo);

        GCoinPaymentOrder gcoinPayment = gcoinPaymentRepository.findOneByOrderTypeAndOutTradeNo(orderType, outTradeNo);

        //支付订单不存在
        if (gcoinPayment == null) {
            logger.error("createRefund >> exception : {} ", BizExceptionEnum.GCOIN_REFUND_PAYMENT_ORDER_NOT_EXIST.getErrMsg());
            throw new BusinessException(BizExceptionEnum.GCOIN_REFUND_PAYMENT_ORDER_NOT_EXIST);
        }

        String stateCode = gcoinPayment.getState();

        //支付订单未付款
        if (GCoinPaymentStateType.WAIT_FOR_PAYMENT.getCode().equals(stateCode)) {
            logger.error("createRefund >> exception : {} ", BizExceptionEnum.GCOIN_REFUND_PAYMENT_ORDER_NOT_PAY.getErrMsg());
            throw new BusinessException(BizExceptionEnum.GCOIN_REFUND_PAYMENT_ORDER_NOT_PAY);
        }

        //支付订单已全部结清，不可再退款
        if (GCoinPaymentStateType.NON_REFUNDABLE.getCode().equals(stateCode)) {
            logger.error("createRefund >> exception : {} ", BizExceptionEnum.GCOIN_REFUND_PAYMENT_ORDER_SETTLED.getErrMsg());
            throw new BusinessException(BizExceptionEnum.GCOIN_REFUND_PAYMENT_ORDER_SETTLED);
        }

        //支付订单支付失败
        if (GCoinPaymentStateType.PAYMENT_FAILED.getCode().equals(stateCode) || GCoinPaymentStateType.NON_REFUNDABLE.getCode().equals(stateCode)) {
            logger.error("createRefund >> exception : {} ", BizExceptionEnum.GCOIN_REFUND_ORDER_CLOSED.getErrMsg());
            throw new BusinessException(BizExceptionEnum.GCOIN_REFUND_ORDER_CLOSED);
        }

        return gcoinPayment;
    }

    @Override
    public GCoinRefundOrder checkGcoinRefundState(String orderId) {
        GCoinRefundOrder gcoinRefund = gcoinRefundRepository.findOneByOrderId(orderId);
        //退款订单不存在
        if (gcoinRefund == null) {
            logger.debug("checkGcoinRefundState >> orderId : {} ,exception :{} ", orderId, BizExceptionEnum.GCOIN_REFUND_ORDER_NOT_EXIST.getErrMsg());
            throw new BusinessException(BizExceptionEnum.GCOIN_REFUND_ORDER_NOT_EXIST);
        }

        //退款订单已经完成
        if (!GCoinRefundStateType.CREATE.getCode().equals(gcoinRefund.getState())) {
            logger.debug("checkGcoinRefundState >> orderId :{} ,exception :{} ", orderId, BizExceptionEnum.GCOIN_REFUND_ORDER_FINISHED.getErrMsg());
            throw new BusinessException(BizExceptionEnum.GCOIN_REFUND_ORDER_FINISHED);
        }

        return gcoinRefund;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRefundInfo(String refundOrderId) throws BusinessException {
        logger.debug("updateRefundInfo >> start");
        logger.debug("updateRefundInfo >> refundOrderId :{}", refundOrderId);
        GCoinRefundOrder gcoinRefund = gcoinRefundRepository.findOneByOrderId(refundOrderId);

        String outTradeNo = gcoinRefund.getOutTradeNo();
        String orderType = gcoinRefund.getOrderType();
        BigDecimal refundAmount = gcoinRefund.getAmount();
        String userId = gcoinRefund.getUserId();

        //获取支付订单信息
        GCoinPaymentOrder gcoinPayment = gcoinPaymentRepository.findOneByOrderTypeAndOutTradeNo(orderType, outTradeNo);
        String paymentOrderId = gcoinPayment.getOrderId();
        BigDecimal totalAmount = gcoinPayment.getAmount();
        BigDecimal oldRefundingAmount = gcoinPayment.getRefundingAmount();
        BigDecimal oldRefundedAmount = gcoinPayment.getRefundedAmount();

        //新的退款中金额 = 旧的退款中金额-退款金额
        BigDecimal newRefundingAmount = oldRefundingAmount.subtract(refundAmount);
        gcoinPayment.setRefundingAmount(newRefundingAmount);
        //新的已退款金额 = 旧的已退款金额+退款金额
        BigDecimal newRefundedAmount = oldRefundedAmount.add(refundAmount);
        gcoinPayment.setRefundedAmount(newRefundedAmount);

        //如果支付订单已经全部退清，则对应的支付订单状态更改为已结清，不可再退款
        if (totalAmount.compareTo(newRefundedAmount) == 0) {
            gcoinPayment.setState(GCoinPaymentStateType.NON_REFUNDABLE.getCode());
        }

        //更新支付订单信息表
        String paymentKey = String.format(RedisKey.GCOIN_PAYMENT_KEY, userId, paymentOrderId);
        cacheManager.del(paymentKey);
        gcoinPaymentRepository.save(gcoinPayment);
        logger.debug("updateRefundInfo >> gcoinPayment :{}", gcoinPayment);

        cacheManager.set(paymentKey, gcoinPayment, 24 * 60 * 60);

        //更新退款信息表
        gcoinRefund.setState(GCoinRefundStateType.SUCCESS.getCode());
        String completedDate = DateUtil.fromDate2Str(new Date());
        gcoinRefund.setCompletedDate(completedDate);

        String refundKey = String.format(RedisKey.GCOIN_REFUND_KEY, userId, refundOrderId);
        cacheManager.del(refundKey);
        gcoinRefundRepository.save(gcoinRefund);

        logger.debug("updateRefundInfo >> gcoinRefund :{}", gcoinRefund);
        cacheManager.set(refundKey, gcoinRefund, 24 * 60 * 60);

        //暴鸡币账户金额增加
        GCoinBalance gCoinBalance = gCoinBalanceRepository.findOneByUserId(userId);
        if (gCoinBalance == null) {
            gCoinBalance = AccountUtil.generateGcoinBalance(userId);
        }

        BigDecimal oldTotalBalance = gCoinBalance.getGcoinBalance();
        BigDecimal oldUsableAmount = gCoinBalance.getUsableAmount();
        gCoinBalance.setGcoinBalance(oldTotalBalance.add(refundAmount));
        gCoinBalance.setUsableAmount(oldUsableAmount.add(refundAmount));

        //更新用户暴鸡币账户状态
        gCoinBalanceRepository.save(gCoinBalance);

        logger.debug("updateRefundInfo >> gCoinBalance :{}", gCoinBalance);
        //加入一条退款流水记录
        gcoinRefund.setBalance(gCoinBalance.getUsableAmount().setScale(2, BigDecimal.ROUND_DOWN).toString());
        billFlowService.saveRecord(gcoinRefund);

        logger.debug("updateRefundInfo >> end ");

    }

    private GCoinRefundVo exchangeToVo(GCoinRefundOrder gcoinRefund) {
        GCoinRefundVo vo = new GCoinRefundVo();
        vo.setOrderId(gcoinRefund.getOrderId());
        vo.setUserId(gcoinRefund.getUserId());
        vo.setOrderType(Integer.valueOf(gcoinRefund.getOrderType()));
        vo.setOutRefundNo(gcoinRefund.getOutRefundNo());
        vo.setOutTradeNo(gcoinRefund.getOutTradeNo());
        vo.setCompletedDate(gcoinRefund.getCompletedDate());
        vo.setAmount(gcoinRefund.getAmount().multiply(new BigDecimal(100)));
        vo.setAttach(gcoinRefund.getAttach());
        vo.setBody(gcoinRefund.getBody());
        vo.setDescription(gcoinRefund.getDescription());
        vo.setSubject(gcoinRefund.getSubject());
        vo.setState(gcoinRefund.getState());
        return vo;
    }

    /**
     * @Description: 消费暴鸡币退款消息
     * @Param: [mqSimpleOrder]
     * @Return java.lang.Boolean
     * @Author: xusisi
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean consumeRefundMQ(MQSimpleOrder mqSimpleOrder) {
        //订单ID
        String orderId = mqSimpleOrder.getOrderId();
        //回调URL
        String notifyUrl = mqSimpleOrder.getNotifyUrl();
        ResponsePacket result = null;
        GCoinRefundVo vo = new GCoinRefundVo();
        GCoinRefundOrder gcoinRefund = null;

        //消息是否消费锁标识
        String consumerLock = String.format(RedisKey.GCOIN_REFUND_LOCK_KEY, orderId);
        //判断这个订单是否正在被消费
        RLock lock = cacheManager.redissonClient().getLock(consumerLock);
        if (lock.isLocked()) {
            logger.debug("GCoinRefundConsumer >> mq 已经被消费 orderId : {} ", orderId);
            return true;
        }
        gcoinRefund = this.checkGcoinRefundState(orderId);
        if (gcoinRefund.getState().equals(GCoinRefundStateType.SUCCESS.getCode())) {
            return true;
        }

        //mq标记
        Boolean flag = true;
        try {
            //设置锁失效时间为3秒
            lock.lock(3, TimeUnit.SECONDS);
            //更新订单状态、暴鸡币余额、增加流水记录
            this.updateRefundInfo(orderId);
            vo = this.getRefundVo(orderId);
            result = ResponsePacket.onSuccess(vo);
        } catch (BusinessException e) {
            logger.error("GCoinRefundConsumer >> BusinessException : " + e.getErrMsg());
            result = ResponsePacket.onError(e.getErrCode(), e.getErrMsg());
            vo.setOrderId(orderId);
            result.setData(vo);
            flag = true;
        } catch (ApplePayException e) {
            logger.error("GCoinRefundConsumer >> ApplePayException : " + e.getErrMsg());
            result = ResponsePacket.onError(e.hashCode(), e.getErrMsg());
            vo.setOrderId(orderId);
            result.setData(vo);
            flag = false;
        } catch (Exception e) {
            logger.error("GCoinRefundConsumer >> exception : {}", e);
            result = ResponsePacket.onError(e.hashCode(), e.getMessage());
            vo.setOrderId(orderId);
            result.setData(vo);
            flag = false;
        } finally {

            externalPaymentUtil.callBack(notifyUrl, result);
            //处理完解锁
            try {
                if (lock != null && lock.isLocked()) {
                    lock.unlock();
                }
            } catch (Exception e) {
                logger.error("分布式锁系统：{} ", e);
            }
            return flag;
        }
    }

}

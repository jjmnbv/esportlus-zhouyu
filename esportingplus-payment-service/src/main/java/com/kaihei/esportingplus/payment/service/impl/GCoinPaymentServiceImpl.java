package com.kaihei.esportingplus.payment.service.impl;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.algorithm.SnowFlake;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.DateUtil;
import com.kaihei.esportingplus.payment.api.enums.AccountStateType;
import com.kaihei.esportingplus.payment.api.enums.GCoinPaymentErrorInfo;
import com.kaihei.esportingplus.payment.api.enums.GCoinPaymentStateType;
import com.kaihei.esportingplus.payment.api.params.GCoinPaymentCreateParams;
import com.kaihei.esportingplus.payment.api.params.GCoinPaymentUpdateParams;
import com.kaihei.esportingplus.payment.api.vo.GCoinPaymentPreVo;
import com.kaihei.esportingplus.payment.api.vo.GCoinPaymentVo;
import com.kaihei.esportingplus.payment.data.jpa.repository.GCoinBalanceRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.GCoinPaymentRepository;
import com.kaihei.esportingplus.payment.domain.entity.GCoinBalance;
import com.kaihei.esportingplus.payment.domain.entity.GCoinPaymentOrder;
import com.kaihei.esportingplus.payment.mq.message.MQSimpleOrder;
import com.kaihei.esportingplus.payment.mq.producer.GCoinPaymentTransactionProducer;
import com.kaihei.esportingplus.payment.service.BillFlowService;
import com.kaihei.esportingplus.payment.service.GCoinPaymentService;
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
 * 暴鸡币支付Service
 *
 * @author xusisi
 * @create 2018-09-23 15:23
 **/
@Service
public class GCoinPaymentServiceImpl implements GCoinPaymentService {

    private static Logger logger = LoggerFactory.getLogger(GCoinPaymentServiceImpl.class);
    private static final CacheManager cacheManager = CacheManagerFactory.create();

    @Autowired
    private GCoinBalanceRepository gCoinBalanceRepository;

    @Autowired
    private GCoinPaymentRepository gcoinPaymentRepository;

    @Autowired
    private GCoinPaymentTransactionProducer gcoinPaymentTransactionProducer;

    @Autowired
    private BillFlowService billFlowService;
    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private ExternalPaymentUtil externalPaymentUtil;
    //@Autowired
    //private UserInvitShareServiceClient userInvitShareServiceClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GCoinPaymentPreVo preCreatePayment(GCoinPaymentCreateParams gcoinPaymentCreateParams) throws BusinessException {

        /***
         * 判断用户账户是否冻结，账号金额是否足够支付
         * 可以就先将信息保存至redis和MySQL,
         * 冻结支付金额
         * 同时返回对应的支付订单ID，业务订单ID，业务订单类型
         */
        logger.debug("入参 >> GCoinPaymentCreateParams : {}", gcoinPaymentCreateParams);

        String orderId = null;
        String userId = gcoinPaymentCreateParams.getUserId();
        String outTradeNo = gcoinPaymentCreateParams.getOutTradeNo();
        String orderType = gcoinPaymentCreateParams.getOrderType();
        String tempAmount = gcoinPaymentCreateParams.getAmount();
        String attach = gcoinPaymentCreateParams.getAttach();
        Integer intAmount = CommonUtils.strToInteger(tempAmount);

        //获取的金额单位是分，要转换成元为单位的数据
        BigDecimal amount = new BigDecimal(intAmount).divide(new BigDecimal(100));
        //检查暴鸡币账户状态
        GCoinBalance gCoinBalance = this.checkGcoinAccountState(userId);
        BigDecimal usableAmount = gCoinBalance.getUsableAmount();
        if (usableAmount.compareTo(amount) < 0) {
            logger.error(" exception : {}", BizExceptionEnum.GCOIN_ACCOUNT_BALANCE_NOT_ENOUGH.getErrMsg());
            throw new BusinessException(BizExceptionEnum.GCOIN_ACCOUNT_BALANCE_NOT_ENOUGH);
        }

        //创建支付订单加锁
        String createGcoinPaymentLock = String.format(RedisKey.CREATE_GCOIN_PAYMENT_LOCK_KEY, orderType, outTradeNo);

        //判断该业务订单是否正在支付
        RLock lock = cacheManager.redissonClient().getLock(createGcoinPaymentLock);
        if (lock.isLocked()) {
            logger.warn("该订单正在处理中 >> orderType : {} ,outTradeNo :{} ", orderType, outTradeNo);
            logger.error("exception : {} ", BizExceptionEnum.GCOIN_PAYMENT_ORDER_CREATING.getErrMsg());
            throw new BusinessException(BizExceptionEnum.GCOIN_PAYMENT_ORDER_CREATING);
        }

        try {
            lock.lock(3, TimeUnit.SECONDS);

            GCoinPaymentOrder gcoinPayment = gcoinPaymentRepository.findOneByOrderTypeAndOutTradeNo(orderType, outTradeNo);
            /****
             * 订单支付时，根据业务订单号，业务订单类型，支付订单状态判断，
             * 1、订单不存在，直接创建新支付订单
             * 2、订单存在，需要判断订单是否已经支付完成，是否已经结清，是否关闭
             * 2.1已经支付成功，则提示用户，该订单已经支付成功，
             * 2.2已经结清，或失败，则提示用户该订单不可再进行支付，请重新下业务订单
             */
            if (gcoinPayment != null) {
                orderId = gcoinPayment.getOrderId();
                String stateCode = gcoinPayment.getState();

                if (GCoinPaymentStateType.NON_REFUNDABLE.getCode().equals(stateCode) || GCoinPaymentStateType.PAYMENT_FAILED.getCode().equals(stateCode)) {
                    logger.error("preCreatePayment >> exception : {}", BizExceptionEnum.GCOIN_PAYMENT_ORDER_CLOSED.getErrMsg());
                    throw new BusinessException(BizExceptionEnum.GCOIN_PAYMENT_ORDER_CLOSED);
                }

                if (!GCoinPaymentStateType.WAIT_FOR_PAYMENT.getCode().equals(stateCode)) {
                    logger.error("preCreatePayment >> exception : {}", BizExceptionEnum.GCOIN_PAYMENT_ORDER_PAIED.getErrMsg());
                    throw new BusinessException(BizExceptionEnum.GCOIN_PAYMENT_ORDER_PAIED);
                }

            } else {
                orderId = String.valueOf(snowFlake.nextId());
                gcoinPayment = new GCoinPaymentOrder();
                gcoinPayment.setOrderId(orderId);
                gcoinPayment.setUserId(userId);
                gcoinPayment.setSubject(gcoinPaymentCreateParams.getSubject());
                gcoinPayment.setBody(gcoinPaymentCreateParams.getBody());
                gcoinPayment.setDescription(gcoinPaymentCreateParams.getDescription());
                gcoinPayment.setSourceId(gcoinPaymentCreateParams.getSourceId());
                gcoinPayment.setAmount(amount);
                gcoinPayment.setAttach(attach);
                gcoinPayment.setOutTradeNo(outTradeNo);
                gcoinPayment.setOrderType(orderType);
                gcoinPayment.setRefundingAmount(new BigDecimal(0));
                gcoinPayment.setRefundedAmount(new BigDecimal(0));
                //订单状态待支付
                gcoinPayment.setState(GCoinPaymentStateType.WAIT_FOR_PAYMENT.getCode());
                //将订单信息保存至数据库
                gcoinPaymentRepository.save(gcoinPayment);

                String key = String.format(RedisKey.GCOIN_PAYMENT_KEY, userId, orderId);

                //将订单信息存于redis中，过期时间为24小时
                logger.debug("saveToRedis >> key : {} ", key);
                cacheManager.set(key, gcoinPayment, 24 * 60 * 60);

                //冻结暴鸡币账户中的待支付金额
                BigDecimal frozenAmount = gCoinBalance.getFrozenAmount();
                //冻结金额 = 原冻结金额+支付金额
                gCoinBalance.setFrozenAmount(frozenAmount.add(amount));
                //可用金额 = 原可用金额- 支付金额
                gCoinBalance.setUsableAmount(usableAmount.subtract(amount));
                //更新暴鸡币账号信息
                gCoinBalanceRepository.save(gCoinBalance);
            }

            GCoinPaymentPreVo gcoinPaymentPreVo = new GCoinPaymentPreVo();
            gcoinPaymentPreVo.setOrderId(orderId);
            gcoinPaymentPreVo.setOrderType(Integer.valueOf(orderType));
            gcoinPaymentPreVo.setOutTradeNo(outTradeNo);

            logger.debug("出参 >>>> gcoinPaymentPreVo : {}", gcoinPaymentPreVo);
            return gcoinPaymentPreVo;
        } catch (BusinessException e) {
            throw e;
        } finally {
            try {
                if (lock != null && lock.isLocked()) {
                    lock.unlock();
                }
            } catch (Exception e) {
                logger.error("释放分布式锁异常");
            }

        }

    }

    /***
     * 将订单的支付信息保存至MQ
     *
     * @param gcoinPaymentUpdateParams
     * @throws BusinessException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public GCoinPaymentVo updateOrderInfo(GCoinPaymentUpdateParams gcoinPaymentUpdateParams)
            throws BusinessException {

        logger.debug("入参 >> gcoinPaymentUpdateParams : {} ", gcoinPaymentUpdateParams);
        String userId = gcoinPaymentUpdateParams.getUserId();
        String orderId = gcoinPaymentUpdateParams.getOrderId();
        String outTradeNo = gcoinPaymentUpdateParams.getOutTradeNo();

        //校验暴鸡币支付订单状态
        GCoinPaymentOrder gcoinPayment = this.checkOrderState(orderId, userId);
        logger.debug("gcoinPayment : {}", gcoinPayment);

        //发送暴鸡币支付订单ID至MQ
        try {

            MQSimpleOrder mqSimpleOrder = new MQSimpleOrder();
            String notifyUrl = gcoinPaymentUpdateParams.getNotifyUrl();
            mqSimpleOrder.setNotifyUrl(notifyUrl);
            mqSimpleOrder.setOrderId(orderId);
            mqSimpleOrder.setOutTradeNo(outTradeNo);
            mqSimpleOrder.setUserId(userId);

            Message message = MessageBuilder.of(mqSimpleOrder).topic(RocketMQConstant.PAYMENT_TOPIC).tag(RocketMQConstant.GCOIN_PAYMENT_TAG).build();
            message.setTransactionId(CommonUtils.genTransactionId(RocketMQConstant.PAYMENT_TRANSACTION_ID, orderId));

            logger.debug("sendMessage >> start >> topic :{} , tag :{} , message : {} , gcoinPaymentUpdateParams : {} ",
                    RocketMQConstant.PAYMENT_TOPIC,
                    RocketMQConstant.GCOIN_PAYMENT_TAG,
                    message,
                    gcoinPaymentUpdateParams);

            gcoinPaymentTransactionProducer.sendMessageInTransaction(message, gcoinPaymentUpdateParams);

            logger.debug("sendMessage >> end >> topic :{} , tag :{} , message : {}  gcoinPaymentUpdateParams : {} ",
                    RocketMQConstant.PAYMENT_TOPIC,
                    RocketMQConstant.GCOIN_PAYMENT_TAG,
                    message,
                    gcoinPaymentUpdateParams);

        } catch (Exception e) {
            logger.error("updatePaymentInfo >> exception :{} ", e.getMessage());
            throw new BusinessException(BizExceptionEnum.ROCKETMQ_PRODUCER_ERROR);
        }

        GCoinPaymentVo vo = this.getGcoinPaymentVo(orderId, userId);
        logger.debug("出参 >> GcoinRechargeVo : {} ", vo);
        return vo;

    }

    @Override
    public GCoinPaymentOrder getPaymentInfo(String orderId, String userId) {
        logger.debug("getPaymentInfo >> start >> orderId : {} ,userId : {} ", orderId, userId);

        //先从redis中查询，如果不存在，则从数据库获取
        GCoinPaymentOrder gcoinPayment = this.getPaymentInfoFromRedis(orderId, userId);

        if (gcoinPayment == null) {
            gcoinPayment = gcoinPaymentRepository.findOneByOrderIdAndUserId(orderId, userId);
        }

        logger.debug("getPaymentInfo >> end >> gcoinPayment : {} ", gcoinPayment);
        return gcoinPayment;
    }

    @Override
    public GCoinPaymentOrder getPaymentInfoFromRedis(String orderId, String userId) {
        logger.debug("getPaymentInfoFromRedis >> start >> orderId : {} ,userId : {} ", orderId, userId);

        String key = String.format(RedisKey.GCOIN_PAYMENT_KEY, userId, orderId);

        logger.debug("getPaymentInfoFromRedis >> getFromRedis >> key : {} ", key);

        //将订单信息存于redis中，过期时间为24小时
        GCoinPaymentOrder gcoinPayment = cacheManager.get(key, GCoinPaymentOrder.class);

        logger.debug("getPaymentInfoFromRedis >> gcoinPayment : {} ", key);
        return gcoinPayment;
    }

    /***
     * 超时未支付的订单，订单状态将会被改成关闭
     *
     * @param orderId
     * @param userId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refreshPaymentInfo(String orderId, String userId) {

        logger.debug("refreshPaymentInfo >> start >> orderId :{} ,userId : {}", orderId, userId);

        GCoinPaymentOrder gcoinPayment = this.getPaymentInfo(orderId, userId);

        logger.debug("refreshPaymentInfo >> gcoinPayment : {} ", gcoinPayment);

        if (gcoinPayment == null) {
            logger.warn("refreshPaymentInfo >> error :" + BizExceptionEnum.GCOIN_PAYMENT_ORDER_NOT_EXIST.getErrMsg());
            //throw new BusinessException(BizExceptionEnum.GCOIN_PAYMENT_ORDER_NOT_EXIST);
            return;
        }
        //如果订单为待支付以外的状态，都不处理
        String stateCode = gcoinPayment.getState();
        if (!GCoinPaymentStateType.WAIT_FOR_PAYMENT.getCode().equals(stateCode)) {
            logger.debug("refreshPaymentInfo >> end >> orderId : {} 该订单状态已经处理完毕，无需再刷新状态。", orderId);
            return;
        }

        gcoinPayment.setState(GCoinPaymentStateType.PAYMENT_FAILED.getCode());

        gcoinPaymentRepository.save(gcoinPayment);

        //将冻结金额解冻
        //获取支付订单金额
        BigDecimal gcoinPaymentAmount = gcoinPayment.getAmount();
        GCoinBalance gCoinBalance = gCoinBalanceRepository.findOneByUserId(userId);
        if (gCoinBalance == null) {
            logger.warn("refreshPaymentInfo >> userId :{} exception : {}", userId, BizExceptionEnum.GCOIN_ACCOUNT_BALANCE_NOT_EXIST.getErrMsg());
        }
        //旧的冻结金额
        BigDecimal frozenAmount = gCoinBalance.getFrozenAmount();
        //旧的可用金额
        BigDecimal usableAmount = gCoinBalance.getUsableAmount();
        //新的冻结金额 = 旧的冻结金额-支付订单金额
        gCoinBalance.setFrozenAmount(frozenAmount.subtract(gcoinPaymentAmount));
        //新的可用金额 = 新的可用金额+支付订单金额
        gCoinBalance.setUsableAmount(usableAmount.add(gcoinPaymentAmount));
        //更新用户的暴鸡币账户信息
        gCoinBalanceRepository.save(gCoinBalance);

        logger.debug("refreshPaymentInfo >> end >> orderId : {} 该订单因超时未处理，状态变更为支付失败。", orderId);
    }

    @Override
    public GCoinPaymentVo getGcoinPaymentVo(String orderId, String userId) {
        GCoinPaymentVo vo = new GCoinPaymentVo();
        GCoinPaymentOrder gcoinPayment = this.getPaymentInfo(orderId, userId);

        vo.setBody(gcoinPayment.getBody());
        vo.setDescription(gcoinPayment.getDescription());
        vo.setSubject(gcoinPayment.getSubject());
        vo.setSourceId(gcoinPayment.getSourceId());
        vo.setAmount(gcoinPayment.getAmount().multiply(new BigDecimal(100)));
        vo.setOutTradeNo(gcoinPayment.getOutTradeNo());
        vo.setOrderType(Integer.valueOf(gcoinPayment.getOrderType()));
        vo.setUserId(gcoinPayment.getUserId());
        vo.setOrderId(gcoinPayment.getOrderId());
        vo.setAttach(gcoinPayment.getAttach());
        vo.setState(gcoinPayment.getState());
        vo.setCompletedDate(gcoinPayment.getCompletedDate());
        vo.setRefundedAmount(gcoinPayment.getRefundedAmount());
        vo.setRefundingAmount(gcoinPayment.getRefundingAmount());

        return vo;
    }

    /***
     * 检查支付订单的状态
     *
     * @param orderId
     * @param userId
     */
    @Override
    public GCoinPaymentOrder checkOrderState(String orderId, String userId)
            throws BusinessException {
        logger.debug("checkOrderState >>  end >> orderId: {} , userId :{} ", orderId, userId);

        //校验订单是否存在：先从redis查询，再去数据库查
        GCoinPaymentOrder gcoinPayment = this.getPaymentInfo(orderId, userId);

        logger.debug("checkOrderState >> end >> gcoinPayment: {}", gcoinPayment);

        //支付订单不存在
        if (gcoinPayment == null) {
            logger.error("checkOrderState >> exception : {} ", BizExceptionEnum.GCOIN_PAYMENT_ORDER_NOT_EXIST.getErrMsg());
            throw new BusinessException(BizExceptionEnum.GCOIN_PAYMENT_ORDER_NOT_EXIST);
        }
        String stateCode = gcoinPayment.getState();

        //判断订单是否支付失败
        if (GCoinPaymentStateType.PAYMENT_FAILED.getCode().equals(stateCode)) {
            logger.error("checkOrderState >> exception : {} ", BizExceptionEnum.GCOIN_PAYMENT_ORDER_CLOSED.getErrMsg());
            throw new BusinessException(BizExceptionEnum.GCOIN_PAYMENT_ORDER_CLOSED);
        }

        //判断订单是否已经支付成功
        if (!GCoinPaymentStateType.WAIT_FOR_PAYMENT.getCode().equals(stateCode)) {
            logger.error("checkOrderState >> exception : {} ", BizExceptionEnum.GCOIN_PAYMENT_ORDER_PAIED.getErrMsg());
            throw new BusinessException(BizExceptionEnum.GCOIN_PAYMENT_ORDER_PAIED);
        }

        return gcoinPayment;
    }

    @Override
    public GCoinPaymentOrder checkOrderState(String orderId) throws BusinessException {
        GCoinPaymentOrder gcoinPayment = gcoinPaymentRepository.findOneByOrderId(orderId);

        //支付订单不存在
        if (gcoinPayment == null) {
            logger.error("checkOrderState >> exception : {} ", BizExceptionEnum.GCOIN_PAYMENT_ORDER_NOT_EXIST.getErrMsg());
            throw new BusinessException(BizExceptionEnum.GCOIN_PAYMENT_ORDER_NOT_EXIST);
        }
        String userId = gcoinPayment.getUserId();

        return this.checkOrderState(orderId, userId);
    }

    /***
     * 跟进用户ID判断用户的暴鸡币账户状态
     *
     * @param userId
     */
    @Override
    public GCoinBalance checkGcoinAccountState(String userId) throws BusinessException {

        logger.debug("checkGcoinAccountState >> start >> userId : {} ", userId);

        GCoinBalance gCoinBalance = gCoinBalanceRepository.findOneByUserId(userId);

        logger.debug("checkGcoinAccountState >> end >> gCoinBalance : {} ", gCoinBalance);

        //暴鸡币账户不存在，说明余额肯定不足以支付
        if (gCoinBalance == null) {
            logger.error("executePayment >> Exception :{} ", BizExceptionEnum.GCOIN_ACCOUNT_BALANCE_NOT_ENOUGH.getErrMsg());
            throw new BusinessException(BizExceptionEnum.GCOIN_ACCOUNT_BALANCE_NOT_ENOUGH);
        }

        //暴鸡币账户不可用
        if (AccountStateType.UNAVAILABLE.getCode().equals(gCoinBalance.getState())) {
            logger.error("executePayment >> Exception :{} ",
                    BizExceptionEnum.GCOIN_ACCOUNT_UNAVAILABLE.getErrMsg());
            throw new BusinessException(BizExceptionEnum.GCOIN_ACCOUNT_UNAVAILABLE);
        }

        //暴鸡币账户被冻结
        if (AccountStateType.FROZEN.getCode().equals(gCoinBalance.getState())) {
            logger.error("executePayment >> Exception :{} ", BizExceptionEnum.GCOIN_ACCOUNT_FROZEN.getErrMsg());
            throw new BusinessException(BizExceptionEnum.GCOIN_ACCOUNT_FROZEN);
        }

        return gCoinBalance;
    }

    /***
     * 更新暴鸡币支付订单状态、更新用户暴鸡币余额、新增一条流水记录
     *
     * @param orderId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePaymentInfo(String orderId) {
        logger.debug("入参 >> orderId : {} ", orderId);

        GCoinPaymentOrder gcoinPayment = gcoinPaymentRepository.findOneByOrderId(orderId);
        gcoinPayment.setState(GCoinPaymentStateType.WAIT_FOR_SETTLE.getCode());
        String completedDate = DateUtil.fromDate2Str(new Date());
        gcoinPayment.setCompletedDate(completedDate);
        gcoinPayment.setLastModifiedDate(new Date());
        String userId = gcoinPayment.getUserId();

        //先清缓存，然后保存数据库，数据库保存成功再添加到缓存中
        String key = String.format(RedisKey.GCOIN_PAYMENT_KEY, userId, orderId);
        cacheManager.del(key);
        //更新提现订单
        gcoinPaymentRepository.save(gcoinPayment);

        //将最新的payment信息保存进redis
        //订单成功以后保存30分钟
        cacheManager.set(key, gcoinPayment, 30 * 60);

        //更新用暴鸡币余额表
        GCoinBalance gCoinBalance = this.checkGcoinAccountState(userId);

        //先获取用户原来的总暴鸡币余额
        BigDecimal oldTotalGcoinBalance = gCoinBalance.getGcoinBalance();
        //原来的冻结的暴鸡币金额
        BigDecimal oldFrozenGcoinBalance = gCoinBalance.getFrozenAmount();
        //本次支付的暴鸡币数量
        BigDecimal paymentGcoin = gcoinPayment.getAmount();
        //最新余额
        BigDecimal newTotalGcoinBalance = oldTotalGcoinBalance.subtract(paymentGcoin);
        BigDecimal newFrozenGcoinBalance = oldFrozenGcoinBalance.subtract(paymentGcoin);
        gCoinBalance.setGcoinBalance(newTotalGcoinBalance);
        gCoinBalance.setFrozenAmount(newFrozenGcoinBalance);

        gCoinBalance.setLastModifiedDate(new Date());

        //更新用户暴鸡币余额信息
        gCoinBalanceRepository.save(gCoinBalance);

        gcoinPayment.setBalance(gCoinBalance.getUsableAmount().setScale(2, BigDecimal.ROUND_DOWN).toString());
        //增加暴鸡币支付流水
        billFlowService.saveRecord(gcoinPayment);

        //调用暴鸡币消费奖励通知接口
        //CoinConsumeEvent param = new CoinConsumeEvent();
        //param.setUid(userId);
        //Integer coin = paymentGcoin.multiply(new BigDecimal(100)).intValue();
        //param.setCoin(coin);
        //param.setPayOrderNo(orderId);
        //
        //ResponsePacket resp = null;
        //String req = JacksonUtils.toJson(param);
        //try {
        //    logger.debug("调用暴鸡币消费奖励接口 start >> req:{}", req);
        //    resp = userInvitShareServiceClient.awardTask(param);
        //
        //} catch (Exception e) {
        //    logger.error("调用暴鸡币消费奖励接口 exceptoin : {}", e);
        //}
        //if (resp == null || !resp.responseSuccess()) {
        //    logger.error("调用暴鸡币消费奖励接口 >>> req={} | resp={} ", req, JacksonUtils.toJson(resp));
        //}
        //
        //logger.debug("调用暴鸡币消费奖励接口 end >> req={} | resp={} ", req, JacksonUtils.toJson(resp));
    }

    /**
     * 设置用于返回的错误信息
     */
    @Override
    public GCoinPaymentVo setErrorInfo(GCoinPaymentVo vo, BusinessException e) {
        int errorCode = e.getErrCode();

        if (errorCode == BizExceptionEnum.GCOIN_PAYMENT_ORDER_CLOSED.getErrCode()) {
            vo.setErrorCode(GCoinPaymentErrorInfo.ORDER_TIMEOUT.getCode());
            vo.setErrorMsg(GCoinPaymentErrorInfo.ORDER_TIMEOUT.getMsg());

        } else if (errorCode == BizExceptionEnum.GCOIN_ACCOUNT_FROZEN.getErrCode()) {
            vo.setErrorCode(GCoinPaymentErrorInfo.BALANCE_NOT_ENOUGH.getCode());
            vo.setErrorMsg(GCoinPaymentErrorInfo.BALANCE_NOT_ENOUGH.getMsg());

        } else if (errorCode == BizExceptionEnum.GCOIN_ACCOUNT_BALANCE_NOT_ENOUGH.getErrCode()) {
            vo.setErrorCode(GCoinPaymentErrorInfo.ACCOUNT_IS_FROZEN.getCode());
            vo.setErrorMsg(GCoinPaymentErrorInfo.ACCOUNT_IS_FROZEN.getMsg());

        }

        return vo;
    }

    @Override
    public GCoinPaymentVo getVo(String orderId, String orderType, String outTradeNo) throws BusinessException {
        GCoinPaymentOrder gcoinPayment = null;
        GCoinPaymentVo vo = null;
        Boolean flag1 = true;
        if (orderId != null && !"".equals(orderId.trim())) {
            gcoinPayment = gcoinPaymentRepository.findOneByOrderId(orderId);
        } else {
            flag1 = false;
        }
        if (gcoinPayment != null) {
            String userId = gcoinPayment.getUserId();
            vo = this.getGcoinPaymentVo(orderId, userId);
            return vo;
        }

        Boolean flag2 = orderType != null && !"".equals(orderType.trim());
        Boolean flag3 = outTradeNo != null && !"".equals(outTradeNo.trim());

        Boolean flag = flag1 || (flag2 && flag3);

        //有一个不成立时
        if (!flag) {
            logger.error("getVo >> exception :{} ", BizExceptionEnum.GCOIN_PAYMENT_SEARCH_PARAM_ERROR.getErrMsg());
            throw new BusinessException(BizExceptionEnum.GCOIN_PAYMENT_SEARCH_PARAM_ERROR);
        }

        gcoinPayment = gcoinPaymentRepository.findOneByOrderTypeAndOutTradeNo(orderType, outTradeNo);

        if (gcoinPayment == null) {
            return null;
        }

        String userId = gcoinPayment.getUserId();
        orderId = gcoinPayment.getOrderId();
        vo = this.getGcoinPaymentVo(orderId, userId);

        return vo;
    }

    /**
     * @Description: 消费暴鸡币支付消息
     * @Param: [mqSimpleOrder]
     * @Return java.lang.Boolean
     * @Author: xusisi
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean consumePaymentMQ(MQSimpleOrder mqSimpleOrder) {

        //订单ID
        String orderId = mqSimpleOrder.getOrderId();
        //回调URL
        String notifyUrl = mqSimpleOrder.getNotifyUrl();
        ResponsePacket result = null;
        GCoinPaymentVo vo = new GCoinPaymentVo();
        GCoinPaymentOrder gcoinPayment = null;

        //消息是否消费锁标识
        String consumerLock = String.format(RedisKey.GCOIN_PAYMENT_LOCK_KEY, orderId);
        //判断这个订单是否正在被消费
        RLock lock = cacheManager.redissonClient().getLock(consumerLock);
        if (lock.isLocked()) {
            logger.info(">> mq 已经被消费 orderId : {} ", orderId);
            return true;
        }
        //校验订单状态
        gcoinPayment = this.checkOrderState(orderId);
        //mq标记
        Boolean flag = true;
        try {
            //设置锁失效时间为3秒
            lock.lock(3, TimeUnit.SECONDS);
            String userId = gcoinPayment.getUserId();
            //更新订单状态、暴鸡币余额、增加流水记录
            this.updatePaymentInfo(orderId);
            vo = this.getGcoinPaymentVo(orderId, userId);
            result = ResponsePacket.onSuccess(vo);
        } catch (BusinessException e) {
            logger.info("业务异常，告知MQ无需再次发送消息，不需要再次消费了");
            logger.error("executePayment >> BusinessException : " + e.getErrMsg());
            result = ResponsePacket.onError(e.getErrCode(), e.getErrMsg());
            vo.setOrderId(orderId);
            this.setErrorInfo(vo, e);
            result.setData(vo);
            flag = true;
        } catch (Exception e) {
            logger.info("其他系统异常，需要告知MQ再次发送消息，需要重复消费消息了。");
            logger.error("executePayment >> exception : {}", e);
            result = ResponsePacket.onError(e.hashCode(), e.getMessage());
            vo.setOrderId(orderId);
            result.setData(vo);
            flag = false;
        } finally {
            externalPaymentUtil.callBack(notifyUrl, result);
            try {
                //处理完解锁
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

package com.kaihei.esportingplus.payment.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.algorithm.SnowFlake;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.payment.annotation.CheckAndAutowireChannelSetting;
import com.kaihei.esportingplus.payment.api.enums.AlipayPayStateEnum;
import com.kaihei.esportingplus.payment.api.enums.ExternalOrderPayChannelType;
import com.kaihei.esportingplus.payment.api.enums.ExternalPayStateEnum;
import com.kaihei.esportingplus.payment.api.enums.ExternalRefundStateEnum;
import com.kaihei.esportingplus.payment.api.params.CloseOrCancelPayOrderParams;
import com.kaihei.esportingplus.payment.api.params.CloudAccountOrderParams;
import com.kaihei.esportingplus.payment.api.params.PayOrderParams;
import com.kaihei.esportingplus.payment.api.params.RefundOrderParams;
import com.kaihei.esportingplus.payment.api.vo.CloudAccountRespVo;
import com.kaihei.esportingplus.payment.api.vo.CreateRefundOrderReturnVo;
import com.kaihei.esportingplus.payment.data.jpa.repository.ExternalPaymentOrderRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.ExternalRefundOrderRepository;
import com.kaihei.esportingplus.payment.data.mongodb.repository.PaymentVoucherRepository;
import com.kaihei.esportingplus.payment.data.mongodb.repository.RefundVoucherRepository;
import com.kaihei.esportingplus.payment.domain.document.PaymentVoucher;
import com.kaihei.esportingplus.payment.domain.document.RefundVoucher;
import com.kaihei.esportingplus.payment.domain.entity.AbstractEntity;
import com.kaihei.esportingplus.payment.domain.entity.AlipaySetting;
import com.kaihei.esportingplus.payment.domain.entity.ExternalPaymentOrder;
import com.kaihei.esportingplus.payment.domain.entity.ExternalRefundOrder;
import com.kaihei.esportingplus.payment.mq.message.ExternalPayOrderMQ;
import com.kaihei.esportingplus.payment.mq.message.ExternalRefundOrderMQ;
import com.kaihei.esportingplus.payment.mq.producer.AlipayNotifyProductor;
import com.kaihei.esportingplus.payment.mq.producer.AlipayRefundProductor;
import com.kaihei.esportingplus.payment.service.ExternalTradeBillService;
import com.kaihei.esportingplus.payment.service.PayService;
import com.kaihei.esportingplus.payment.util.*;
import com.maihaoche.starter.mq.base.MessageBuilder;
import org.apache.rocketmq.common.message.Message;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName NewAlipayServiceImpl
 * @Description TODO
 * @Author xusisi
 * @Date 2018/11/22 上午11:58
 */
@Service("alipayService")
public class AlipayServiceImpl implements PayService {

    private static Logger logger = LoggerFactory.getLogger(AlipayServiceImpl.class);

    @Autowired
    private ExternalPaymentUtil externalPaymentUtil;

    @Autowired
    private ExternalRefundUtil externalRefundUtil;

    private static final CacheManager cacheManager = CacheManagerFactory.create();

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private ExternalPaymentOrderRepository externalPaymentOrderRepository;

    @Autowired
    private ExternalRefundOrderRepository externalRefundOrderRepository;

    @Autowired
    private RefundVoucherRepository refundVoucherRepository;

    @Autowired
    private PaymentVoucherRepository paymentVoucherRepository;

    @Autowired
    private AlipayRefundProductor alipayRefundProductor;

    @Autowired
    private AlipayNotifyProductor alipayNotifyProductor;

    @Autowired
    private ExternalTradeBillService externalTradeBillService;

    /**
     * 构建支付订单信息参数
     *
     * @param : [payOrderParams, appId, channelTag, paySetting, ip, area]
     * @Author : xusisi
     **/
    @Transactional(rollbackFor = Exception.class)
    @CheckAndAutowireChannelSetting
    @Override
    public Map<String, String> createPaymentOrder(PayOrderParams payOrderParams, String appId, String channelTag, AbstractEntity paySetting,
                                                  String ip, String area) throws BusinessException {
        //校验信息
        String orderType = payOrderParams.getOrderType();
        String outTradeNo = payOrderParams.getOutTradeNo();
        String orderId;

        Integer totalAmount = payOrderParams.getTotalAmount();
        String userId = payOrderParams.getUserId();
        AlipaySetting alipaySetting = (AlipaySetting) paySetting;
        ExternalPaymentOrder paymentOrder = externalPaymentUtil.getPaymentOrder(orderType, outTradeNo);
        //封装订单参数
        if (paymentOrder != null) {
            externalPaymentUtil.checkPaymentStateWhenCreate(paymentOrder);
            orderId = paymentOrder.getOrderId();
        } else {
            paymentOrder = new ExternalPaymentOrder();
            //创建订单支付记录信息
            orderId = String.valueOf(snowFlake.nextId());
            paymentOrder.setOrderId(orderId);
            paymentOrder.setOutTradeNo(outTradeNo);
            paymentOrder.setOrderType(orderType);
            paymentOrder.setChannelId(alipaySetting.getChannelId());
            paymentOrder.setChannelName(channelTag);
            paymentOrder.setBody(payOrderParams.getBody());
            paymentOrder.setSourceAppId(appId);
            paymentOrder.setState(ExternalPayStateEnum.UNPAIED.getCode());
            paymentOrder.setNotifyUrl(payOrderParams.getNotifyUrl());
            paymentOrder.setCurrencyType(payOrderParams.getCurrencyType());
            paymentOrder.setSubject(payOrderParams.getSubject());
            //支付订单保存金额（分）
            paymentOrder.setTotalFee(totalAmount);
            paymentOrder.setUserId(userId);
            //保存订单信息至MySQL
            externalPaymentOrderRepository.save(paymentOrder);
            //保存信息至redis
            String paymentKey = String.format(RedisKey.EXTERNAL_PAYMENT_ORDER_KEY, orderType, outTradeNo);
            logger.debug("save key : {} ", paymentKey);
            //订单超时未支付时间关闭时间：30分钟
            cacheManager.set(paymentKey, paymentOrder, RedisKey.SAVE_DATA_TIME);
        }

        //构造支付请求参数
        Map<String, Object> resultMap = AlipayUtil.createPaymentInfo(paymentOrder, alipaySetting);

        // 解析支付返回结果
        AlipayTradeAppPayRequest alipayTradeAppPayRequest = (AlipayTradeAppPayRequest) resultMap.get("requestParams");
        AlipayTradeAppPayResponse alipayTradeAppPayResponse = (AlipayTradeAppPayResponse) resultMap.get("responseParams");
        //校验返回结果
        AlipayUtil.checkResponse(alipayTradeAppPayResponse);

        //保存一份至MongoDB
        PaymentVoucher paymentVoucher = paymentVoucherRepository.findOneByOrderId(orderId);
        if (paymentVoucher == null) {
            paymentVoucher = new PaymentVoucher();
            paymentVoucher.setOrderId(orderId);
            paymentVoucher.setAppId(appId);
            paymentVoucher.setRequestMethod(alipayTradeAppPayRequest.getApiMethodName());
            paymentVoucher.setRequestParams(FastJsonUtils.toJson(alipayTradeAppPayRequest));
            paymentVoucher.setRequestUrl(alipaySetting.getRequestUrl());
            paymentVoucher.setState(ExternalPayStateEnum.UNPAIED.getCode());
            paymentVoucherRepository.save(paymentVoucher);
        }

        //封装数据返回客户端
        String orderStr = alipayTradeAppPayResponse.getBody();
        logger.debug("返回支付宝的加密订单信息 >> orderStr : {} ", orderStr);
        Map<String, String> vo = new HashMap<>();
        vo.put("sign", orderStr);
        vo.put("order_id", orderId);
        vo.put("sign_type", alipaySetting.getSignType());
        logger.debug("出参 >> vo : {}", vo);

        return vo;
    }

    /**
     * 查询支付订单信息
     *
     * @param : [appId, channelTag, outTradeNo, paySetting, orderType]
     * @Author : xusisi
     **/
    @Transactional(rollbackFor = Exception.class)
    @CheckAndAutowireChannelSetting
    @Override
    public ExternalPaymentOrder searchPaymentOrderInfo(String outTradeNo, String appId, String channelTag, AbstractEntity paySetting,
                                                       String orderType) throws BusinessException {

        AlipaySetting alipaySetting = (AlipaySetting) paySetting;
        // 校验订单信息
        ExternalPaymentOrder paymentOrder = externalPaymentUtil.getPaymentOrder(orderType, outTradeNo);
        //订单不存在
        if (paymentOrder == null) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_PAY_ORDER_NOT_FOUND.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_PAY_ORDER_NOT_FOUND);
        }

        //查询订单状态
        String payState = paymentOrder.getState();
        //当订单状态为：未付款、已付款时，对应的订单状态还是可变的。所以需要查询第三方进行确认，其他情况不会再变，则不需要查询第三方了。
        if (!(ExternalPayStateEnum.UNPAIED.getCode().equals(payState) || ExternalPayStateEnum.SUCCESS.getCode().equals(payState))) {
            return paymentOrder;
        }

        // 封装查询参数-调用查询接口
        Map<String, Object> result = AlipayUtil.searchPaymentInfo(paymentOrder, alipaySetting);

        AlipayTradeQueryRequest alipayTradeQueryRequest = (AlipayTradeQueryRequest) result.get("requestParams");
        AlipayTradeQueryResponse alipayTradeQueryResponse = (AlipayTradeQueryResponse) result.get("responseParams");
        // 解析返回结果
        paymentOrder = AlipayUtil.checkSearchPaymentResponse(paymentOrder, alipayTradeQueryResponse);
        // 更新数据信息
        externalPaymentUtil.refreshPaymentInfo(paymentOrder);
        //保存信息到MongoDB
        String orderId = paymentOrder.getOrderId();
        PaymentVoucher paymentVoucher = paymentVoucherRepository.findOneByOrderId(orderId);
        if (paymentVoucher == null) {
            paymentVoucher = new PaymentVoucher();
            paymentVoucher.setOrderId(orderId);
        }
        paymentVoucher.setState(paymentOrder.getState());
        paymentVoucherRepository.save(paymentVoucher);

        //保存支付流水
        if (ExternalPayStateEnum.SUCCESS.getCode().equals(paymentOrder.getState())) {
            externalTradeBillService.saveTradeBill(paymentOrder);
        }

        return paymentOrder;
    }

    /**
     * 发起支付宝退款
     *
     * @param : [refundOrderParams, appId, channelTag, paySetting]
     * @Author : xusisi
     **/
    @Transactional(rollbackFor = Exception.class)
    @CheckAndAutowireChannelSetting
    @Override
    public CreateRefundOrderReturnVo createRefundOrder(RefundOrderParams refundOrderParams, String appId, String channelTag,
                                                       AbstractEntity paySetting) throws BusinessException {

        /***
         * 1、判断支付订单是否存在，
         * 2、判断退款金额是否小于等于订单可退金额，
         * 3、创建退款申请，保存数据至MQ
         * 4、MQ异步消费发起退款请求，
         *
         *  默认TRADE_SUCCESS（交易成功）， TRADE_CLOSED（交易关闭）， TRADE_FINISHED（交易完成） 三种状态均会触发异步通知，
         *  WAIT_BUYER_PAY（交易创建）不触发异步通知
         */
        logger.debug("refundOrderParams : {}", refundOrderParams);

        //申请退款金额（单位分）
        String refundAmountStr = refundOrderParams.getRefundAmount();
        Integer refundAmount = CommonUtils.strToInteger(refundAmountStr);

        //业务订单号
        String outTradeNo = refundOrderParams.getOutTradeNo();
        //业务订单类型
        String orderType = refundOrderParams.getOrderType();
        //原支付订单号
        String payOrderId = refundOrderParams.getPayOrderId();
        //退款订单号
        String outRefundNo = refundOrderParams.getOutRefundNo();
        //退款通知回调URL
        String notifyUrl = refundOrderParams.getNotifyUrl();
        //用户ID
        String userId = refundOrderParams.getUserId();
        String body = refundOrderParams.getBody();
        String description = refundOrderParams.getDescription();
        String subject = refundOrderParams.getSubject();

        //验证支付订单信息
        ExternalPaymentOrder paymentOrder = externalPaymentUtil.getPaymentOrder(orderType, outTradeNo);
        //订单不存在
        if (paymentOrder == null) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_PAY_ORDER_NOT_FOUND.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_PAY_ORDER_NOT_FOUND);
        }

        externalPaymentUtil.checkPaymentStateWhenRefund(paymentOrder);

        ExternalRefundOrder refundOrder = externalRefundUtil.getRefundOrder(outRefundNo);

        if (refundOrder != null) {
            // 校验退款订单状态
            externalRefundUtil.checkRefundOrderWhenRefund(refundOrder);
            // 退款中的订单直接返回
            CreateRefundOrderReturnVo vo = new CreateRefundOrderReturnVo();
            vo.setOrderId(refundOrder.getOrderId());
            vo.setOutTradeNo(outTradeNo);
            vo.setOrderType(orderType);
            vo.setRefundAmount(outRefundNo);
            vo.setPayOrderId(payOrderId);
            vo.setRefundAmount(refundAmountStr);
            return vo;
        }

        //验证是否还有可退金额
        //退款中的金额，可退的金额总和。
        if (StringUtils.isEmpty(payOrderId)) {
            payOrderId = paymentOrder.getOrderId();
        }
        Integer applyRefundAmount = externalRefundUtil.getRefundAmount(payOrderId);
        Integer payAmount = paymentOrder.getTotalFee();

        if ((payAmount - applyRefundAmount) < refundAmount) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_REFUND_ORDER_NOT_ENOUGH_REFUND.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_REFUND_ORDER_NOT_ENOUGH_REFUND);
        }

        AlipaySetting alipaySetting = (AlipaySetting) paySetting;
        //创建新的退款信息
        //订单号
        String orderId = String.valueOf(snowFlake.nextId());
        Long channelId = alipaySetting.getChannelId();
        refundOrder = new ExternalRefundOrder();
        refundOrder.setOutTradeNo(outTradeNo);
        refundOrder.setChannelId(channelId);
        refundOrder.setChannelName(channelTag);
        refundOrder.setOutRefundNo(outRefundNo);
        refundOrder.setOrderId(orderId);
        refundOrder.setPayOrderId(payOrderId);
        refundOrder.setOrderType(orderType);
        refundOrder.setTotalFee(refundAmount);
        refundOrder.setOutRefundNo(outRefundNo);
        refundOrder.setUserId(userId);
        refundOrder.setNotifyUrl(notifyUrl);
        refundOrder.setBody(body);
        refundOrder.setAttach(description);
        refundOrder.setState(subject);
        refundOrder.setState(ExternalRefundStateEnum.REFUNDING.getCode());
        //保存订单信息至数据库
        externalRefundOrderRepository.save(refundOrder);
        //保存退款订单至redis
        String refundKey = String.format(RedisKey.EXTERNAL_REFUND_ORDER_KEY, outRefundNo);
        logger.debug("key : {} ", refundKey);
        cacheManager.set(refundKey, refundOrder, RedisKey.SAVE_DATA_TIME);

        //退款请求数据保存到MongoDB中
        RefundVoucher refundVoucher = new RefundVoucher();
        refundVoucher.setOrderId(orderId);
        refundVoucher.setAppId(appId);
        refundVoucher.setState(ExternalRefundStateEnum.REFUNDING.getCode());
        refundVoucherRepository.save(refundVoucher);

        //订单信息保存成功以后在发送至MQ进行处理
        try {

            ExternalRefundOrderMQ refundOrderMQ = new ExternalRefundOrderMQ();
            refundOrderMQ.setOrderId(orderId);
            refundOrderMQ.setAppId(appId);
            refundOrderMQ.setChannelTag(channelTag);
            refundOrderMQ.setChannelType(ExternalOrderPayChannelType.ALIPAY.getValue());

            Message message =
                    MessageBuilder.of(refundOrderMQ).topic(RocketMQConstant.PAYMENT_TOPIC).tag(RocketMQConstant.EXTERNAL_ALIPAY_REFUND_TAG).build();
            message.setTransactionId(CommonUtils.genTransactionId(RocketMQConstant.PAYMENT_TRANSACTION_ID, orderId));

            logger.debug("sendMessage >> start >> topic :{} , tag :{} , message : {} , refundOrderMQ : {} ",
                    RocketMQConstant.PAYMENT_TOPIC,
                    RocketMQConstant.EXTERNAL_ALIPAY_REFUND_TAG,
                    message,
                    refundOrderMQ);

            alipayRefundProductor.sendMessageInTransaction(message, refundOrderMQ);

            logger.debug("sendMessage >> end >> topic :{} , tag :{} , message : {}  refundOrderMQ : {} ",
                    RocketMQConstant.PAYMENT_TOPIC,
                    RocketMQConstant.EXTERNAL_ALIPAY_REFUND_TAG, message,
                    refundOrderMQ);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("exception :{} ", e.getMessage());
            throw new BusinessException(BizExceptionEnum.ROCKETMQ_PRODUCER_ERROR);
        }

        CreateRefundOrderReturnVo vo = new CreateRefundOrderReturnVo();
        vo.setOrderId(orderId);
        vo.setOutTradeNo(outTradeNo);
        vo.setOrderType(orderType);
        vo.setRefundAmount(outRefundNo);
        vo.setPayOrderId(payOrderId);
        vo.setRefundAmount(refundAmountStr);
        return vo;

    }

    /**
     * 发起退款申请到支付宝，并将结果返回到业务方
     *
     * @param : [refundOrderMQ]
     * @Author : xusisi
     **/
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateRefundInfo(ExternalRefundOrderMQ refundOrderMQ) {

        logger.info("入参 >> refundOrderMQ : {} ", refundOrderMQ);

        String orderId = refundOrderMQ.getOrderId();
        String channelTag = refundOrderMQ.getChannelTag();
        String appId = refundOrderMQ.getAppId();
        String channelType = refundOrderMQ.getChannelType();

        if (!ExternalOrderPayChannelType.ALIPAY.getValue().equals(channelType)) {
            //不是Alipay回调信息，所以不处理
            logger.debug("orderId : {} 不是支付宝支付的退款信息，丢弃不处理", orderId);
            return true;
        }

        ExternalRefundOrder refundOrder = externalRefundOrderRepository.findOneByOrderId(orderId);

        if (refundOrder == null) {
            logger.info("退款订单号 : {} 不存在", orderId);
            return true;
        }
        try {
            externalRefundUtil.checkRefundOrderWhenRefund(refundOrder);
        } catch (BusinessException e) {
            logger.info("退款订单号 ：{} ,{} ", orderId, e.getErrMsg());
            return true;
        }

        //加载Alipay配置信息
        AlipaySetting alipaySetting = null;
        try {
            alipaySetting = (AlipaySetting) externalPaymentUtil.getPaySetting(appId, channelTag, channelType);
        } catch (BusinessException e) {
            logger.info("该appId, channelTag 对应的支付宝配置信息失败。", e.getErrMsg());
            return true;
        }

        //消息是否消费锁标识
        String consumerLock = String.format(RedisKey.EXTERNAL_MQ_LOCK_ALIPAY_REFUND, orderId);
        //判断这个订单是否正在被消费
        RLock lock = cacheManager.redissonClient().getLock(consumerLock);
        if (lock.isLocked()) {
            logger.info(">> mq 已经被消费 orderId : {} ", orderId);
            return true;
        }

        ResponsePacket packet = null;
        Boolean flag = true;

        try {
            lock.lock(3, TimeUnit.SECONDS);
            //发起退款请求
            Map<String, Object> resultResponse = AlipayUtil.createRefund(refundOrder, alipaySetting);
            AlipayTradeRefundRequest alipayTradeRefundRequest = (AlipayTradeRefundRequest) resultResponse.get("requestParams");
            AlipayTradeRefundResponse alipayTradeRefundResponse = (AlipayTradeRefundResponse) resultResponse.get("responseParams");

            String payOrderId = refundOrder.getPayOrderId();
            ExternalPaymentOrder paymentOrder = externalPaymentOrderRepository.findOneByOrderId(payOrderId);
            //校验返回参数信息
            Map<String, Object> resultOrders = AlipayUtil.checkRefundResponse(paymentOrder, refundOrder, alipayTradeRefundResponse);
            paymentOrder = (ExternalPaymentOrder) resultOrders.get("paymentOrder");
            refundOrder = (ExternalRefundOrder) resultOrders.get("refundOrder");

            //更新支付订单信息，更新退款订单信息
            String outTradeNo = paymentOrder.getOutTradeNo();
            String orderType = paymentOrder.getOrderType();

            // 更新数据信息
            externalPaymentUtil.refreshPaymentInfo(paymentOrder);
            externalRefundUtil.refreshRefundInfo(refundOrder);

            //退款成功保存退款流水
            if (ExternalRefundStateEnum.SUCCESS.getCode().equals(refundOrder.getState())) {
                //保存退款流水
                externalTradeBillService.saveTradeBill(refundOrder);
            }

            //更新MongoDB中的退款信息
            RefundVoucher refundVoucher = refundVoucherRepository.findOneByOrderId(orderId);
            if (refundVoucher == null) {
                refundVoucher = new RefundVoucher();
                refundVoucher.setOrderId(orderId);
            }
            refundVoucher.setState(refundOrder.getState());
            refundVoucher.setRequestParams(FastJsonUtils.toJson(alipayTradeRefundRequest));
            refundVoucher.setRequestMethod(alipayTradeRefundRequest.getApiMethodName());
            refundVoucherRepository.save(refundVoucher);

            packet = ResponsePacket.onSuccess(refundOrder);

        } catch (BusinessException e) {

            packet = ResponsePacket.onError();
            packet.setData(refundOrder);
            packet.setCode(e.getErrCode());
            packet.setMsg(e.getErrMsg());

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("exception : {} ", e);
            flag = false;

        } finally {
            //退款通知回调URL
            String notifyUrl = refundOrder.getNotifyUrl();
            //回调通知业务方支付结果
            flag = externalPaymentUtil.callBack(notifyUrl, packet);

            try {
                if (lock != null && lock.isLocked()) {
                    lock.unlock();
                }
            } catch (Exception e) {
                logger.warn("分布式锁解锁异常");
            }
            return flag;

        }

    }

    /**
     * @Description: 查询退款订单信息
     * @Param: [outRefundNo, appId, channelTag, paySetting]
     * @Return com.kaihei.esportingplus.payment.domain.entity.ExternalRefundOrder
     * @Author: xusisi
     */
    @Transactional(rollbackFor = Exception.class)
    @CheckAndAutowireChannelSetting
    @Override
    public ExternalRefundOrder searchRefundOrderInfo(String outRefundNo, String appId, String channelTag, AbstractEntity paySetting) throws BusinessException {

        /****
         * 1、根据退款订单号查询退款金额，
         * 如果该订单已经更新支付宝通知，直接返回退款状态，
         * 如果还未受到支付宝异步通知，则发起退款查询接口去支付宝查询对应的退款信息
         */
        logger.debug("outRefundNo : {} ", outRefundNo);

        ExternalRefundOrder refundOrder = externalRefundUtil.getRefundOrder(outRefundNo);
        if (refundOrder == null) {
            logger.error("exception : {}", BizExceptionEnum.ALIPAY_REFUND_PAYORDER_NOT_EXIST.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_REFUND_ORDER_NOT_EXIST);
        }

        String refundState = refundOrder.getState();
        //判断退款订单状态是否是已经结束的状态（退款成功，退款失败，取消退款）
        if (!ExternalRefundStateEnum.REFUNDING.getCode().equals(refundState)) {
            return refundOrder;
        }

        //向第三方发起退款订单状态查询
        //加载Alipay配置信息
        AlipaySetting alipaySetting = (AlipaySetting) paySetting;
        Map<String, Object> resultResponse = AlipayUtil.searchRefundInfo(refundOrder, alipaySetting);

        AlipayTradeFastpayRefundQueryRequest alipayTradeFastpayRefundQueryRequest = (AlipayTradeFastpayRefundQueryRequest) resultResponse.get(
                "requestParams");
        AlipayTradeFastpayRefundQueryResponse alipayTradeFastpayRefundQueryResponse = (AlipayTradeFastpayRefundQueryResponse) resultResponse.get(
                "responseParams");

        String payOrderId = refundOrder.getPayOrderId();
        ExternalPaymentOrder paymentOrder = externalPaymentOrderRepository.findOneByOrderId(payOrderId);

        Map<String, Object> resultOrder = AlipayUtil.checkSearchRefundResponse(paymentOrder, refundOrder, alipayTradeFastpayRefundQueryResponse);
        paymentOrder = (ExternalPaymentOrder) resultOrder.get("paymentOrder");
        refundOrder = (ExternalRefundOrder) resultOrder.get("refundOrder");

        //更新数据库，缓存中的支付订单信息，退款订单信息
        externalPaymentUtil.refreshPaymentInfo(paymentOrder);
        externalRefundUtil.refreshRefundInfo(refundOrder);

        //退款成功保存退款流水
        if (ExternalRefundStateEnum.SUCCESS.getCode().equals(refundOrder.getState())) {
            //保存退款流水
            externalTradeBillService.saveTradeBill(refundOrder);
        }

        //更新MongoDB中的退款订单信息
        String orderId = refundOrder.getOrderId();
        RefundVoucher refundVoucher = refundVoucherRepository.findOneByOrderId(orderId);
        if (refundVoucher == null) {
            refundVoucher = new RefundVoucher();
            refundVoucher.setOrderId(orderId);
        }
        refundVoucher.setState(ExternalRefundStateEnum.SUCCESS.getCode());
        refundVoucher.setRequestMethod(alipayTradeFastpayRefundQueryRequest.getApiMethodName());
        refundVoucher.setRequestParams(FastJsonUtils.toJson(alipayTradeFastpayRefundQueryRequest));
        refundVoucher.setMetadata(FastJsonUtils.toJson(alipayTradeFastpayRefundQueryResponse));
        refundVoucherRepository.save(refundVoucher);
        //返回最新退款订单信息给用户
        return refundOrder;
    }

    /**
     * @Description: 关闭支付订单
     * @Param: [closeOrCancelPayOrderParams, appId, channelTag, paySetting]
     * @Return com.kaihei.esportingplus.payment.domain.entity.ExternalPaymentOrder
     * @Author: xusisi
     */
    @Transactional(rollbackFor = Exception.class)
    @CheckAndAutowireChannelSetting
    @Override
    public ExternalPaymentOrder closePaymentOrder(CloseOrCancelPayOrderParams closeOrCancelPayOrderParams, String appId, String channelTag,
                                                  AbstractEntity paySetting) throws BusinessException {
        /***
         * 1、校验订单是否存在，
         * 2、校验订单是否已经支付，
         * 3、发起订单关闭操作，
         * 4、更新本地订单支付状态
         */

        String outTradeNo = closeOrCancelPayOrderParams.getOutTradeNo();
        String orderType = closeOrCancelPayOrderParams.getOrderType();
        ExternalPaymentOrder paymentOrder = externalPaymentUtil.getPaymentOrder(orderType, outTradeNo);
        if (paymentOrder == null) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_PAY_ORDER_NOT_FOUND.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_PAY_ORDER_NOT_FOUND);
        }
        //校验订单状态
        externalPaymentUtil.checkPaymentStateWhenClose(paymentOrder);

        //加载Alipay配置信息
        AlipaySetting alipaySetting = (AlipaySetting) paySetting;
        Map<String, Object> resultResponse = AlipayUtil.closePayment(paymentOrder, alipaySetting);
        AlipayTradeCloseRequest alipayTradeCloseRequest = (AlipayTradeCloseRequest) resultResponse.get("requestParams");
        AlipayTradeCloseResponse alipayTradeCloseResponse = (AlipayTradeCloseResponse) resultResponse.get("responseParams");

        // 校验返回的结果
        paymentOrder = AlipayUtil.checkClosePaymentResponse(paymentOrder, alipayTradeCloseResponse);

        //更新数据库，缓存中的支付订单信息
        externalPaymentUtil.refreshPaymentInfo(paymentOrder);

        //将关闭订单请求信息保存至MongoDB中
        PaymentVoucher paymentVoucher = paymentVoucherRepository.findOneByOrderId(paymentOrder.getOrderId());
        paymentVoucher.setState(ExternalPayStateEnum.CLOSING.getCode());
        paymentVoucher.setCloseRequestMethod(alipayTradeCloseRequest.getApiMethodName());
        paymentVoucher.setCloseRequestParams(FastJsonUtils.toJson(alipayTradeCloseRequest));
        paymentVoucher.setCloseTimestamp(LocalDateTime.now());
        paymentVoucher.setCloseMetadata(FastJsonUtils.toJson(alipayTradeCloseResponse));
        paymentVoucherRepository.save(paymentVoucher);

        return paymentOrder;

    }

    /**
     * @Description: 撤销支付订单信息
     * @Param: [closeOrCancelPayOrderParams, appId, channelTag, paySetting]
     * @Return com.kaihei.esportingplus.payment.domain.entity.ExternalPaymentOrder
     * @Author: xusisi
     */
    @Transactional(rollbackFor = Exception.class)
    @CheckAndAutowireChannelSetting
    @Override
    public ExternalPaymentOrder cancelPaymentOrder(CloseOrCancelPayOrderParams closeOrCancelPayOrderParams, String appId, String channelTag,
                                                   AbstractEntity paySetting) throws BusinessException {
        //校验订单参数信息

        String orderType = closeOrCancelPayOrderParams.getOrderType();
        String outTradeNo = closeOrCancelPayOrderParams.getOutTradeNo();
        ExternalPaymentOrder paymentOrder = externalPaymentUtil.getPaymentOrder(orderType, outTradeNo);
        if (paymentOrder == null) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_PAY_ORDER_NOT_FOUND.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_PAY_ORDER_NOT_FOUND);
        }
        externalPaymentUtil.checkPaymentStateWhenCancel(paymentOrder);
        // 调用查询支付宝订单接口
        AlipaySetting alipaySetting = (AlipaySetting) paySetting;
        paymentOrder = this.searchPaymentOrderInfo(outTradeNo, appId, channelTag, alipaySetting, orderType);

        //如果订单状态确定则直接调用退款API，生成退款记录，订单状态为撤销
        String tradeStatus = paymentOrder.getState();

        if (AlipayPayStateEnum.TRADE_FINISHED.getCode().equals(tradeStatus)) {
            logger.error("订单已经结束,不可撤销");
            logger.error("exception : {} ", BizExceptionEnum.ALIPAY_CANCEL_PAYORDER_FINISHED.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_CANCEL_PAYORDER_FINISHED);
        }

        if (AlipayPayStateEnum.TRADE_CLOSED.getCode().equals(tradeStatus)) {
            logger.debug("订单已经关闭，不可撤销");
            logger.error("exception : {} ", BizExceptionEnum.ALIPAY_CANCEL_PAYORDER_CLOSED.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_CANCEL_PAYORDER_CLOSED);
        }

        if (AlipayPayStateEnum.TRADE_SUCCESS.getCode().equals(tradeStatus)) {
            logger.debug("订单已经支付成功，请调用退款接口进行操作");
            logger.error("exception : {} ", BizExceptionEnum.ALIPAY_CANCEL_PAYORDER_SUCCESS.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_CANCEL_PAYORDER_SUCCESS);
        }

        Map<String, Object> resultResponse = AlipayUtil.cancelPayment(paymentOrder, alipaySetting);
        AlipayTradeCancelRequest alipayTradeCancelRequest = (AlipayTradeCancelRequest) resultResponse.get("requestParams");
        AlipayTradeCancelResponse alipayTradeCancelResponse = (AlipayTradeCancelResponse) resultResponse.get("responseParams");

        paymentOrder = AlipayUtil.checkCancelPaymentResponse(paymentOrder, alipayTradeCancelResponse);
        //更新数据库，缓存中的支付订单信息
        externalPaymentUtil.refreshPaymentInfo(paymentOrder);
        //更新MongoDB中的信息
        String orderId = paymentOrder.getOrderId();
        PaymentVoucher paymentVoucher = paymentVoucherRepository.findOneByOrderId(orderId);
        if (paymentVoucher == null) {
            paymentVoucher = new PaymentVoucher();
            paymentVoucher.setOrderId(orderId);
        }
        paymentVoucher.setCancelRequestMethod(alipayTradeCancelRequest.getApiMethodName());
        paymentVoucher.setCancelRequestParams(FastJsonUtils.toJson(alipayTradeCancelRequest));
        paymentVoucher.setCancelMetadata(FastJsonUtils.toJson(alipayTradeCancelResponse));
        paymentVoucher.setState(paymentOrder.getState());
        paymentVoucherRepository.save(paymentVoucher);

        return paymentOrder;
    }

    /**
     * @Description: 处理支付宝的回调信息
     * @Param: [params, appId, channelTag, paySetting, type]
     * @Return java.lang.Boolean
     * @Author: xusisi
     */
    @Transactional(rollbackFor = Exception.class)
    @CheckAndAutowireChannelSetting
    @Override
    public Boolean handleNotify(Map<String, String> params, String appId, String channelTag, AbstractEntity paySetting, String type) throws BusinessException,
            AlipayApiException {
        /***
         * 1、从支付宝回调的request中获取值
         * 2、封装必要参数
         * 3、验证签名
         * 4、对签名处理
         * 程序执行完后必须打印输出“success”（不包含引号）。如果商户反馈给支付宝的字符不是success这7个字符，支付宝服务器会不断重发通知，直到超过24小时22分钟。
         * 一般情况下，25小时以内完成8次通知（通知的间隔频率一般是：4m,10m,10m,1h,2h,6h,15h）；
         */
        AlipaySetting alipaySetting = (AlipaySetting) paySetting;

        ExternalPaymentOrder paymentOrder = null;

        paymentOrder = AlipayUtil.checkAlipayNotifyInfo(params, alipaySetting);

        String respTradeStatus = paymentOrder.getState();
        String respOrderId = paymentOrder.getOrderId();
        Integer respAmount = paymentOrder.getTotalFee();

        //校验订单号
        ExternalPaymentOrder externalPaymentOrder = externalPaymentOrderRepository.findOneByOrderId(respOrderId);
        if (externalPaymentOrder == null) {
            logger.error("exception : {} ", BizExceptionEnum.ALIPAY_PAY_ORDER_NOT_FOUND.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_PAY_ORDER_NOT_FOUND);
        }

        //校验支付金额
        Integer totalFee = externalPaymentOrder.getTotalFee();
        if (!respAmount.equals(totalFee)) {
            logger.error("exception :{} ", BizExceptionEnum.ALIPAY_PAY_ORDER_MONEY_NOT_EQUAL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_PAY_ORDER_MONEY_NOT_EQUAL);
        }

        String state = externalPaymentOrder.getState();
        //订单状态为支付成功，并且回调通知状态还是成功，这不需要更新订单信息
        if (ExternalPayStateEnum.SUCCESS.getCode().equals(state) && AlipayPayStateEnum.TRADE_SUCCESS.getCode().equals(respTradeStatus)) {
            logger.info("支付交易已经完成");
            return true;
        }

        Boolean flag = false;
        try {
            //将回调信息保存至MongoDB中
            PaymentVoucher paymentVoucher = paymentVoucherRepository.findOneByOrderId(respOrderId);
            if (paymentVoucher == null) {
                logger.error("exception : {} ", BizExceptionEnum.MONGODB_ALIPAY_PAYMENT_ORDER_NOT_EXITST.getErrMsg());
                throw new BusinessException(BizExceptionEnum.MONGODB_ALIPAY_PAYMENT_ORDER_NOT_EXITST);
            }

            Map<String, Object> metadata = new HashMap<>();
            logger.debug("params : {} ", params);
            metadata.put(respTradeStatus, params);
            paymentVoucher.setMetadata(metadata);

            if (paymentVoucher.getTimestamp() == null) {
                paymentVoucher.setTimestamp(LocalDateTime.now());
            }
            //更新凭证数据信息
            paymentVoucherRepository.save(paymentVoucher);

            //信息保存成功以后发送MQ消息
            ExternalPayOrderMQ externalPayOrderMQ = new ExternalPayOrderMQ();
            externalPayOrderMQ.setOrderState(respTradeStatus);
            externalPayOrderMQ.setOrderId(respOrderId);
            externalPayOrderMQ.setAppId(appId);
            externalPayOrderMQ.setChannelTag(channelTag);
            externalPayOrderMQ.setChannelType(ExternalOrderPayChannelType.ALIPAY.getValue());

            Message message =
                    MessageBuilder.of(externalPayOrderMQ).topic(RocketMQConstant.PAYMENT_TOPIC).tag(RocketMQConstant.EXTERNAL_ALIPAY_NOTIFY_TAG).build();
            message.setTransactionId(CommonUtils.genTransactionId(RocketMQConstant.PAYMENT_TRANSACTION_ID, respOrderId));

            logger.debug("sendMessage >> start >> topic :{} , tag :{} , message : {} , externalPayOrderMQ : {} ",
                    RocketMQConstant.PAYMENT_TOPIC,
                    RocketMQConstant.EXTERNAL_ALIPAY_NOTIFY_TAG,
                    message,
                    externalPayOrderMQ);

            alipayNotifyProductor.sendMessageInTransaction(message, externalPayOrderMQ);

            logger.debug("sendMessage >> end >> topic :{} , tag :{} , message : {}  externalPayOrderMQ : {} ",
                    RocketMQConstant.PAYMENT_TOPIC,
                    RocketMQConstant.EXTERNAL_ALIPAY_NOTIFY_TAG, message,
                    externalPayOrderMQ);

            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("updatePaymentInfo >> exception :{} ", e.getMessage());
            throw new BusinessException(BizExceptionEnum.ROCKETMQ_PRODUCER_ERROR);

        }
        return flag;

    }

    /**
     * @Description: 消费支付宝回调的消息信息
     * @Param: [externalPayOrderMQ]
     * @Return java.lang.Boolean
     * @Author: xusisi
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean consumerAlipayCallBackInfo(ExternalPayOrderMQ externalPayOrderMQ) {

        logger.info("process >> start >> externalPayOrderMQ : {} ", externalPayOrderMQ);

        //MongoDB中的订单id
        String orderId = externalPayOrderMQ.getOrderId();
        //返回的订单状态
        String orderState = externalPayOrderMQ.getOrderState();
        String channelType = externalPayOrderMQ.getChannelType();

        if (!ExternalOrderPayChannelType.ALIPAY.getValue().equals(channelType)) {
            //不是Alipay回调信息，所以不处理
            logger.debug("orderId : {} 不是支付宝支付回调信息，丢弃不处理", orderId);
            return true;
        }

        ResponsePacket result = null;
        ExternalPaymentOrder externalPaymentOrder = null;

        //消息是否消费锁标识
        String consumerLock = String.format(RedisKey.EXTERNAL_MQ_LOCK_ALIPAY_NOTIFY, orderId);
        //判断这个订单是否正在被消费
        RLock lock = cacheManager.redissonClient().getLock(consumerLock);
        if (lock.isLocked()) {
            logger.info(">> mq 已经被消费 orderId : {} ", orderId);
            return true;
        }

        //mq标记
        Boolean flag = true;
        try {
            //设置锁失效时间为3秒
            lock.lock(3, TimeUnit.SECONDS);
            //获取对应的回调参数信息
            PaymentVoucher paymentVoucher = paymentVoucherRepository.findOneByOrderId(orderId);
            if (paymentVoucher == null) {
                logger.info("没有对应的MongoDB支付信息");
                return true;
            }
            HashMap<String, Map<String, String>> metadata = (HashMap<String, Map<String, String>>) paymentVoucher.getMetadata();
            if (metadata == null || metadata.get(orderState) == null) {
                logger.info("没有对应回调信息");
                return true;
            }

            Map<String, String> params = metadata.get(orderState);
            //原支付请求的商户订单号
            String respOrderId = params.get(AlipayConstants.RETURN_PARAM_OUT_TRADE_NO);
            //本次交易支付的订单金额，单位为人民币（元）
            String respAmount = params.get(AlipayConstants.RETURN_PARAM_TOTAL_AMOUNT);

            //获取数据库中订单信息
            externalPaymentOrder = externalPaymentOrderRepository.findOneByOrderId(respOrderId);

            //校验订单状态信息
            //订单交易关闭
            if (externalPaymentOrder == null) {
                logger.info("没有对应的支付订单信息");
                return true;
            }

            //支付金额，支付状态幂等处理
            Integer totalFee = externalPaymentOrder.getTotalFee();
            if (new BigDecimal(respAmount).compareTo(new BigDecimal(totalFee).divide(new BigDecimal(100))) != 0) {
                logger.info("支付金额不一致！");
                return true;
            }

            //订单状态幂等处理
            String state = externalPaymentOrder.getState();
            if (ExternalPayStateEnum.CLOSED.getCode().equals(state)) {
                logger.info("订单已经处理过，不需要重复处理");
                return true;
            }

            externalPaymentOrder = AlipayUtil.handleAlipayCallBackInfo(params, externalPaymentOrder);

            externalPaymentUtil.refreshPaymentInfo(externalPaymentOrder);

            //支付成功保存支付流水
            if (ExternalPayStateEnum.SUCCESS.getCode().equals(externalPaymentOrder.getState())) {
                //保存支付流水
                externalTradeBillService.saveTradeBill(externalPaymentOrder);
            }

            flag = true;
            result = ResponsePacket.onSuccess(externalPaymentOrder);

        } catch (Exception e) {
            logger.error("exception : {}", e);
            flag = false;
            result = ResponsePacket.onError(e.hashCode(), e.getMessage());

        } finally {

            String notifyUrl = externalPaymentOrder.getNotifyUrl();
            flag = externalPaymentUtil.callBack(notifyUrl, result);

            try {
                if (lock != null && lock.isLocked()) {
                    lock.unlock();
                }
            } catch (Exception e) {
                logger.error("分布式锁异常 :{} ", e);
            }
            return flag;

        }

    }

    @Override
    public void walletNotify(Map<String, String> params) {
    }

    @Override
    public CloudAccountRespVo createWithdrawOrder(CloudAccountOrderParams orderParams, String appId, String channelTag, AbstractEntity paySetting,
                                                  String ip) throws BusinessException {
        return null;
    }
}

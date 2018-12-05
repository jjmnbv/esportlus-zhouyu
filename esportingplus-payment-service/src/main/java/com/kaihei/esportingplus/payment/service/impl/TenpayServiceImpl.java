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
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.payment.annotation.CheckAndAutowireChannelSetting;
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
import com.kaihei.esportingplus.payment.domain.entity.ExternalPaymentOrder;
import com.kaihei.esportingplus.payment.domain.entity.ExternalRefundOrder;
import com.kaihei.esportingplus.payment.domain.entity.TenpaySetting;
import com.kaihei.esportingplus.payment.mq.message.ExternalPayOrderMQ;
import com.kaihei.esportingplus.payment.mq.message.ExternalRefundOrderMQ;
import com.kaihei.esportingplus.payment.mq.producer.TenpayPayNotifyProductor;
import com.kaihei.esportingplus.payment.mq.producer.TenpayRefundNotifyProductor;
import com.kaihei.esportingplus.payment.mq.producer.TenpayRefundProductor;
import com.kaihei.esportingplus.payment.service.ExternalTradeBillService;
import com.kaihei.esportingplus.payment.service.PayService;
import com.kaihei.esportingplus.payment.util.*;
import com.maihaoche.starter.mq.base.MessageBuilder;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.rocketmq.common.message.Message;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName TenpayServiceImpl
 * @Description TODO
 * @Author xusisi
 * @Date 2018/11/24 下午2:32
 */
@Service("tenpayService")
public class TenpayServiceImpl implements PayService {

    public static Logger logger = LoggerFactory.getLogger(TenpayServiceImpl.class);

    private static final CacheManager cacheManager = CacheManagerFactory.create();

    @Autowired
    private ExternalPaymentUtil externalPaymentUtil;

    @Autowired
    private ExternalRefundUtil externalRefundUtil;

    @Autowired
    private PaymentVoucherRepository paymentVoucherRepository;

    @Autowired
    private ExternalRefundOrderRepository externalRefundOrderRepository;

    @Autowired
    private RefundVoucherRepository refundVoucherRepository;

    @Autowired
    private TenpayRefundProductor tenpayRefundProductor;

    @Autowired
    private ExternalPaymentOrderRepository externalPaymentOrderRepository;

    @Autowired
    private TenpayPayNotifyProductor tenpayPayNotifyProductor;

    @Autowired
    private TenpayRefundNotifyProductor tenpayRefundNotifyProductor;

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private ExternalTradeBillService externalTradeBillService;

    /**
     * 创建预支付订单信息
     *
     * @param : [payOrderParams, appId, channelTag, paySetting, ip, area]
     * @Author : zhouyu,xusisi
     **/
    @CheckAndAutowireChannelSetting
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, String> createPaymentOrder(PayOrderParams payOrderParams, String appId, String channelTag, AbstractEntity paySetting,
                                                  String ip, String area) throws BusinessException {

        logger.info("入参 >>>");
        logger.info("PayOrderParams : {} , ip : {} , area : {}", payOrderParams, ip, area);
        logger.info("appId : {} , channelTag : {}", appId, channelTag);
        //1.加载配置信息
        TenpaySetting tenpaySetting = (TenpaySetting) paySetting;
        //2.先查缓存和数据库的订单状态
        String outTradeNo = payOrderParams.getOutTradeNo();
        String orderType = payOrderParams.getOrderType();
        Integer totalAmount = payOrderParams.getTotalAmount();

        ExternalPaymentOrder paymentOrder = externalPaymentUtil.getPaymentOrder(orderType, outTradeNo);

        String orderId = null;
        if (paymentOrder != null) {
            //预定订单已经存在,校验订单是否已经操作完
            logger.debug("支付订单已经存在 >> ExternalPaymentOrder :{} ", paymentOrder);
            externalPaymentUtil.checkPaymentStateWhenCreate(paymentOrder);
            orderId = paymentOrder.getOrderId();
        } else {
            paymentOrder = new ExternalPaymentOrder();
            paymentOrder.setUserId(payOrderParams.getUserId());
            orderId = String.valueOf(snowFlake.nextId());
            paymentOrder.setOrderId(orderId);
            paymentOrder.setOutTradeNo(outTradeNo);
            paymentOrder.setOrderType(orderType);
            paymentOrder.setTotalFee(totalAmount);
            paymentOrder.setChannelName(channelTag);
            paymentOrder.setChannelId(tenpaySetting.getChannelId());
            paymentOrder.setSourceAppId(appId);
            paymentOrder.setNotifyUrl(payOrderParams.getNotifyUrl());
            paymentOrder.setSubject(payOrderParams.getSubject());
            paymentOrder.setBody(payOrderParams.getSubject());

        }
        //创建预支付订单信息
        Map<String, String> resultMap = null;
        try {
            resultMap = TenpayUtil.createPrePaymentInfo(paymentOrder, ip, channelTag, tenpaySetting);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_TENPAY_CREATE_PAYMENT_FAIL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_CREATE_PAYMENT_FAIL);
        }

        String respBody = resultMap.get("requestParams");
        String respStr = resultMap.get("responseParams");

        //解析返回参数
        paymentOrder = TenpayUtil.checkPrepayResponse(respStr, tenpaySetting, paymentOrder);

        //保存订单信息
        externalPaymentUtil.refreshPaymentInfo(paymentOrder);
        PaymentVoucher paymentVoucher = paymentVoucherRepository.findOneByOrderId(orderId);
        if (paymentVoucher == null) {
            paymentVoucher = new PaymentVoucher();
            paymentVoucher.setOrderId(orderId);
        }
        paymentVoucher.setState(paymentOrder.getState());
        paymentVoucher.setRequestParams(respStr);
        paymentVoucher.setAppId(appId);
        paymentVoucherRepository.save(paymentVoucher);

        //返回VO
        HashMap<String, String> returnMap = new HashMap<>();
        returnMap.put("appid", tenpaySetting.getAppId());
        returnMap.put("partnerid", tenpaySetting.getMchId());
        returnMap.put("prepayid", paymentOrder.getPrePayId());
        returnMap.put("packageX", "Sign=WXPay");
        returnMap.put("noncestr", RandomStringUtils.random(32, true, true));
        returnMap.put("timestamp", String.valueOf(DateUtil.getTimeStrampSeconds()));
        returnMap.put("sign", PaySignUtils.generateSignature(returnMap, tenpaySetting.getApiSecret(), tenpaySetting.getSignType()));
        returnMap.put("sign_type", tenpaySetting.getSignType());
        returnMap.put("order_id", orderId);
        logger.info("出参 >> returnMap : {}", returnMap);
        return returnMap;

    }

    /**
     * @Description: 查询支付订单信息
     * @Param: [outTradeNo, appId, channelTag, paySetting, orderType]
     * @Return com.kaihei.esportingplus.payment.domain.entity.ExternalPaymentOrder
     * @Author: xusisi
     */
    @CheckAndAutowireChannelSetting
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ExternalPaymentOrder searchPaymentOrderInfo(String outTradeNo, String appId, String channelTag, AbstractEntity paySetting,
                                                       String orderType) throws BusinessException {
        logger.info("入参 >>> ");
        logger.info("outTradeNo : {} , orderType : {} , appId : {} , channelTag : {}", outTradeNo, orderType, appId, channelTag);
        TenpaySetting tenpaySetting = (TenpaySetting) paySetting;
        ExternalPaymentOrder paymentOrder = externalPaymentUtil.getPaymentOrder(orderType, outTradeNo);
        logger.debug("paymentOrder : {} ", paymentOrder);
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
        //向第三方查询支付订单信息
        String orderId = paymentOrder.getOrderId();
        Map<String, String> resultMap = TenpayUtil.queryPaymentInfo(orderId, channelTag, tenpaySetting);
        String respBody = resultMap.get("requestParams");
        String respStr = resultMap.get("responseParams");

        //解析返回参数
        paymentOrder = TenpayUtil.checkQueryPaymentInfo(respStr, tenpaySetting, paymentOrder);

        //更新订单状态
        if (ExternalPayStateEnum.SUCCESS.getCode().equals(paymentOrder.getState())) {
            externalPaymentUtil.refreshPaymentInfo(paymentOrder);
        }

        //保存支付成流水
        if (ExternalPayStateEnum.SUCCESS.getCode().equals(paymentOrder.getState())) {
            externalTradeBillService.saveTradeBill(paymentOrder);
        }

        return paymentOrder;
    }

    /**
     * 接收业务方提交的退款请求
     *
     * @param : [refundOrderParams, appId, channelTag, paySetting]
     * @Author : xusisi
     **/
    @CheckAndAutowireChannelSetting
    @Transactional(rollbackFor = Exception.class)
    @Override
    public CreateRefundOrderReturnVo createRefundOrder(RefundOrderParams refundOrderParams, String appId, String channelTag,
                                                       AbstractEntity paySetting) throws BusinessException {

        logger.info("入参 >> ");
        logger.info("RefundOrderParams :{} ", refundOrderParams);
        logger.info("appId :{} ,channelTag :{}", appId, channelTag);

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
        //申请退款的金额
        String refundAmountStr = refundOrderParams.getRefundAmount();
        Integer refundAmount = CommonUtils.strToInteger(refundAmountStr);

        String body = refundOrderParams.getBody();
        String description = refundOrderParams.getDescription();
        String subject = refundOrderParams.getSubject();

        //验证支付订单信息
        ExternalPaymentOrder paymentOrder = externalPaymentUtil.getPaymentOrder(orderType, outTradeNo);
        logger.debug("ExternalPaymentOrder : {} ", paymentOrder);
        //订单不存在
        if (paymentOrder == null) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_PAY_ORDER_NOT_FOUND.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_PAY_ORDER_NOT_FOUND);
        }
        externalPaymentUtil.checkPaymentStateWhenRefund(paymentOrder);
        //获取退款订单信息
        ExternalRefundOrder refundOrder = externalRefundUtil.getRefundOrder(outRefundNo);
        logger.debug("ExternalRefundOrder : {} ", refundOrder);
        if (refundOrder != null) {
            // 校验退款订单状态
            externalRefundUtil.checkRefundOrderWhenRefund(refundOrder);

            // 退款中的订单直接返回
            CreateRefundOrderReturnVo vo = new CreateRefundOrderReturnVo();
            vo.setOutRefundNo(refundOrder.getOutRefundNo());
            vo.setOrderId(refundOrder.getOrderId());
            vo.setOutTradeNo(outTradeNo);
            vo.setOrderType(orderType);
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

        TenpaySetting tenpaySetting = (TenpaySetting) paySetting;
        //创建新的退款信息
        //订单号
        String orderId = String.valueOf(snowFlake.nextId());
        refundOrder = new ExternalRefundOrder();
        refundOrder.setOutTradeNo(outTradeNo);
        refundOrder.setChannelId(tenpaySetting.getChannelId());
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
        logger.debug("refundKey : {} ", refundKey);
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
            refundOrderMQ.setChannelType(ExternalOrderPayChannelType.TENPAY.getValue());

            Message message =
                    MessageBuilder.of(refundOrderMQ).topic(RocketMQConstant.PAYMENT_TOPIC).tag(RocketMQConstant.EXTERNAL_TENPAY_REFUND_TAG).build();
            message.setTransactionId(CommonUtils.genTransactionId(RocketMQConstant.PAYMENT_TRANSACTION_ID, orderId));

            logger.debug("sendMessage >> start >> topic :{} , tag :{} , message : {} , refundOrderMQ : {} ",
                    RocketMQConstant.PAYMENT_TOPIC,
                    RocketMQConstant.EXTERNAL_TENPAY_REFUND_TAG,
                    message,
                    refundOrderMQ);

            tenpayRefundProductor.sendMessageInTransaction(message, refundOrderMQ);

            logger.debug("sendMessage >> end >> topic :{} , tag :{} , message : {}  refundOrderMQ : {} ",
                    RocketMQConstant.PAYMENT_TOPIC,
                    RocketMQConstant.EXTERNAL_TENPAY_REFUND_TAG, message,
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
        vo.setOutRefundNo(outRefundNo);
        vo.setPayOrderId(payOrderId);
        vo.setRefundAmount(refundAmountStr);
        logger.info("出参 >> vo : {} ", vo);
        return vo;

    }

    /**
     * @Description: 查询退款订单信息
     * @Param: [outRefundNo, appId, channelTag, paySetting]
     * @Return com.kaihei.esportingplus.payment.domain.entity.ExternalRefundOrder
     * @Author: xusisi
     */
    @CheckAndAutowireChannelSetting
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ExternalRefundOrder searchRefundOrderInfo(String outRefundNo, String appId, String channelTag, AbstractEntity paySetting) throws BusinessException {

        logger.info("入参 >> ");
        logger.info("outRefundNo : {} , appId : {} , channelTag :{} ", outRefundNo, appId, channelTag);
        TenpaySetting tenpaySetting = (TenpaySetting) paySetting;
        ExternalRefundOrder refundOrder = externalRefundUtil.getRefundOrder(outRefundNo);
        logger.debug("ExternalRefundOrder : {}", refundOrder);
        if (refundOrder == null) {
            logger.error("exception : {}", BizExceptionEnum.TENPAY_REFUND_ORDER_NOT_EXITS_ERROR.getErrMsg());
            throw new BusinessException(BizExceptionEnum.TENPAY_REFUND_ORDER_NOT_EXITS_ERROR);
        }

        String refundState = refundOrder.getState();
        //判断退款订单状态是否是已经结束的状态（退款成功，退款失败，取消退款）
        if (!ExternalRefundStateEnum.REFUNDING.getCode().equals(refundState)) {
            return refundOrder;
        }

        // 发起退款查询
        Map<String, String> resultMap = TenpayUtil.queryRefundInfo(refundOrder, tenpaySetting, channelTag);
        String respBody = resultMap.get("requestParams");
        String respStr = resultMap.get("responseParams");

        //解析退款返回数据
        refundOrder = TenpayUtil.checkQueryRefundInfo(respStr, tenpaySetting, refundOrder);
        //更新退款订单信息
        externalRefundUtil.refreshRefundInfo(refundOrder);
        String orderId = refundOrder.getOrderId();
        RefundVoucher refundVoucher = refundVoucherRepository.findOneByOrderId(orderId);
        refundVoucher.setState(refundOrder.getState());
        refundVoucherRepository.save(refundVoucher);

        //保存退款成功流水
        if (ExternalRefundStateEnum.SUCCESS.getCode().equals(refundOrder.getState())) {
            externalTradeBillService.saveTradeBill(refundOrder);
        }

        return refundOrder;
    }

    /**
     * @Description: 关闭订单信息
     * @Param: [closeOrCancelPayOrderParams, appId, channelTag, paySetting]
     * @Return com.kaihei.esportingplus.payment.domain.entity.ExternalPaymentOrder
     * @Author: xusisi
     */
    @CheckAndAutowireChannelSetting
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ExternalPaymentOrder closePaymentOrder(CloseOrCancelPayOrderParams closeOrCancelPayOrderParams, String appId, String channelTag,
                                                  AbstractEntity paySetting) throws BusinessException {

        logger.info("入参 >> ");
        logger.info("CloseOrCancelPayOrderParams : {} , appId : {} , channelTag : {} ", closeOrCancelPayOrderParams, appId, channelTag);
        TenpaySetting tenpaySetting = (TenpaySetting) paySetting;
        //查缓存和数据库的订单状态
        String orderType = closeOrCancelPayOrderParams.getOrderType();
        String outTradeNo = closeOrCancelPayOrderParams.getOutTradeNo();
        ExternalPaymentOrder paymentOrder = externalPaymentUtil.getPaymentOrder(orderType, outTradeNo);
        logger.debug("ExternalPaymentOrder :{} ", paymentOrder);
        if (paymentOrder == null) {
            //订单不存在
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_PAY_ORDER_NOT_FOUND.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_PAY_ORDER_NOT_FOUND);
        }
        //校验订单状态
        externalPaymentUtil.checkPaymentStateWhenClose(paymentOrder);

        if (System.currentTimeMillis() - paymentOrder.getCreateDate().getTime() < 5 * 60 * 100) {
            logger.error("微信支付关闭订单错误：{}", BizExceptionEnum.TENPAY_PREPAY_ORDER_CLOSE_TIME_ERROR.getErrMsg());
            throw new BusinessException(BizExceptionEnum.TENPAY_PREPAY_ORDER_CLOSE_TIME_ERROR);
        }

        //发起关闭操作
        Map<String, String> resultMap = TenpayUtil.closePayment(paymentOrder, tenpaySetting, channelTag);
        String respBody = resultMap.get("requestParams");
        String respStr = resultMap.get("responseParams");

        //解析关闭返回数据
        paymentOrder = TenpayUtil.checkClosePaymentResponse(respStr, tenpaySetting, paymentOrder);
        //更新支付订单信息
        externalPaymentUtil.refreshPaymentInfo(paymentOrder);
        String orderId = paymentOrder.getOrderId();
        PaymentVoucher paymentVoucher = paymentVoucherRepository.findOneByOrderId(orderId);
        if (paymentVoucher == null) {
            paymentVoucher = new PaymentVoucher();
            paymentVoucher.setOrderId(orderId);
        }
        paymentVoucher.setState(ExternalPayStateEnum.CLOSED.getCode());
        paymentVoucherRepository.save(paymentVoucher);
        return paymentOrder;

    }

    /**
     * @Description: 处理微信、QQ回调信息
     * @Param: [params, appId, channelTag, paySetting, type]
     * @Return java.lang.Boolean
     * @Author: xusisi
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean handleNotify(Map<String, String> params, String appId, String channelTag, AbstractEntity paySetting, String type) throws BusinessException {

        logger.info("入参 >> ");
        logger.info("params : {} ", params);
        logger.info("appId : {} ,channelTag :{} ,type :{}", appId, channelTag, type);
        TenpaySetting tenpaySetting = (TenpaySetting) paySetting;
        //返回的XML数据
        String xmlStr = params.get("xmlStr");
        //支付回调
        Boolean flag = false;
        if (TenpayConstants.NOTIFY_TYPE_PAYMENT.equalsIgnoreCase(type)) {
            flag = this.handlePaymentNotify(xmlStr, appId, channelTag);
        } else if (TenpayConstants.NOTIFY_TYPE_REFUND.equalsIgnoreCase(type)) {

            flag = this.handleRefundNotify(xmlStr, appId, channelTag, tenpaySetting);
        } else {
            logger.error("type : {} 回调参数错误", type);
            flag = false;
        }

        logger.info("出参 >> flag : {} ", flag);
        return flag;
    }

    /**
     * @Description: 消费支付回调消息
     * @Param: [mq]
     * @Return java.lang.Boolean
     * @Author: xusisi
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean consumerPaymentNotify(ExternalPayOrderMQ mq) {
        String orderId = mq.getOrderId();
        String channelType = mq.getChannelType();

        if (!ExternalOrderPayChannelType.TENPAY.getValue().equals(channelType)) {
            logger.warn("不是微信，QQ的支付回调信息，不处理");
            return true;
        }
        ExternalPaymentOrder paymentOrder = externalPaymentOrderRepository.findOneByOrderId(orderId);
        if (paymentOrder == null) {
            logger.warn("没有对应的支付订单信息");
            return true;
        }
        String state = paymentOrder.getState();
        if (!(ExternalPayStateEnum.UNPAIED.getCode().equals(state) || ExternalPayStateEnum.SUCCESS.getCode().equals(state))) {
            logger.warn("支付订单已经处理完");
            return true;
        }

        PaymentVoucher paymentVoucher = paymentVoucherRepository.findOneByOrderId(orderId);
        if (paymentVoucher == null || paymentVoucher.getMetadata() == null) {
            logger.warn("没有对应的回调通知信息");
            return true;
        }

        String consumerLock = String.format(RedisKey.EXTERNAL_MQ_LOCK_TENPAY_PAY_NOTIFY, orderId);
        RLock lock = cacheManager.redissonClient().getLock(consumerLock);
        if (lock.isLocked()) {
            logger.info("订单正在处理中，orderId : {} ", orderId);
            return true;
        }

        Boolean flag = true;
        try {
            lock.lock(3, TimeUnit.SECONDS);
            Map<String, String> metadata = (Map<String, String>) paymentVoucher.getMetadata();
            String payTimeStr = metadata.get(TenpayConstants.TIME_END);
            Date payTime = DateUtil.str2dateWithYMDHMS(payTimeStr);
            String transactionId = metadata.get(TenpayConstants.TRANSACTION_ID);
            paymentOrder.setTransactionId(transactionId);
            paymentOrder.setPaiedTime(payTime);
            paymentOrder.setState(ExternalPayStateEnum.SUCCESS.getCode());
            externalPaymentUtil.refreshPaymentInfo(paymentOrder);

            paymentVoucher.setCompletedTime(DateUtil.str2LocalDateTime(payTimeStr));
            paymentVoucher.setState(ExternalPayStateEnum.SUCCESS.getCode());
            paymentVoucherRepository.save(paymentVoucher);

            //保存支付流水
            externalTradeBillService.saveTradeBill(paymentOrder);

            //将订单信息发送给业务方
            ResponsePacket packet = ResponsePacket.onSuccess(paymentOrder);
            String notifyUrl = paymentOrder.getNotifyUrl();
            flag = externalPaymentUtil.callBack(notifyUrl, packet);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("消费回调信息时出现异常");
            flag = false;
        } finally {

            try {
                if (lock != null && lock.isLocked()) {
                    lock.isLocked();
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("分布式锁解锁异常");
            }

            return flag;
        }

    }

    /**
     * @Description: 消费退款回调信息
     * @Param: [mq]
     * @Return java.lang.Boolean
     * @Author: xusisi
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean consumerRefundNotify(ExternalRefundOrderMQ mq) {
        String orderId = mq.getOrderId();
        String channelType = mq.getChannelType();

        if (!ExternalOrderPayChannelType.TENPAY.getValue().equals(channelType)) {
            logger.warn("不是微信，QQ的支付回调信息，不处理");
            return true;
        }

        ExternalRefundOrder refundOrder = externalRefundOrderRepository.findOneByOrderId(orderId);
        if (refundOrder == null) {
            logger.warn("没有对应的退款订单");
            return true;
        }
        String state = refundOrder.getState();
        if (!ExternalRefundStateEnum.REFUNDING.getCode().equals(state)) {
            logger.warn("退款订单已经处理完");
            return true;
        }

        RefundVoucher refundVoucher = refundVoucherRepository.findOneByOrderId(orderId);
        if (refundVoucher == null || refundVoucher.getMetadata() == null) {
            logger.warn("没有对应的返回信息");
            return true;
        }

        String consumerLock = String.format(RedisKey.EXTERNAL_MQ_LOCK_TENPAY_PAY_NOTIFY, orderId);
        RLock lock = cacheManager.redissonClient().getLock(consumerLock);
        if (lock.isLocked()) {
            logger.info("订单正在处理中，orderId : {} ", orderId);
            return true;
        }
        Boolean flag = true;
        try {
            lock.expire(3, TimeUnit.SECONDS);

            Map<String, String> metadata = (Map<String, String>) refundVoucher.getMetadata();
            String refundState = metadata.get(TenpayConstants.REFUND_STATUS);

            if (TenpayConstants.REFUND_STATUS_SUCCESS.equals(refundState)) {
                refundOrder.setState(ExternalRefundStateEnum.SUCCESS.getCode());
                String refundTime = metadata.get(TenpayConstants.SUCCESS_TIME);
                refundOrder.setRefundTime(DateUtil.str2dateWithYMDHMS(refundTime));
            } else if (TenpayConstants.REFUND_STATUS_REFUNDCLOSE.equals(refundState)) {
                refundOrder.setState(ExternalRefundStateEnum.FAILED.getCode());
            }
            externalRefundUtil.refreshRefundInfo(refundOrder);
            refundVoucher.setState(refundOrder.getState());
            refundVoucherRepository.save(refundVoucher);

            //保存退款成功流水
            if (ExternalRefundStateEnum.SUCCESS.getCode().equals(refundOrder.getState())) {
                externalTradeBillService.saveTradeBill(refundOrder);
            }

            //退款结果通知到业务方
            String notifyUrl = refundOrder.getNotifyUrl();
            ResponsePacket packet = ResponsePacket.onSuccess(refundOrder);
            flag = externalPaymentUtil.callBack(notifyUrl, packet);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        } finally {
            try {
                if (lock != null && lock.isLocked()) {
                    lock.unlock();
                }
            } catch (Exception e) {
                logger.error("分布式锁解锁异常");
                flag = false;
            }
            return flag;
        }

    }

    /**
     * 异步发送退款请求到第三方
     *
     * @param : [refundOrderMQ]
     * @Author : xusisi
     **/
    @Transactional(rollbackFor = Exception.class)
    public Boolean consumeRefundInfo(ExternalRefundOrderMQ refundOrderMQ) {

        logger.info("入参 >> refundOrderMQ : {} ", refundOrderMQ);

        String orderId = refundOrderMQ.getOrderId();
        String channelTag = refundOrderMQ.getChannelTag();
        String appId = refundOrderMQ.getAppId();
        String channelType = refundOrderMQ.getChannelType();

        if (!ExternalOrderPayChannelType.TENPAY.getValue().equals(channelType)) {
            //不是微信、QQ的回调信息，所以不处理
            logger.debug("orderId : {} 不是微信、QQ支付的退款信息，丢弃不处理", orderId);
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
            logger.info("退款订单号 ：{} ,exception : {} ", orderId, e.getErrMsg());
            return true;
        }

        //加载Alipay配置信息
        TenpaySetting tenpaySetting = null;
        try {
            tenpaySetting = (TenpaySetting) externalPaymentUtil.getPaySetting(appId, channelTag, channelType);
        } catch (BusinessException e) {
            logger.info("该appId, channelTag 对应的支付宝配置信息失败。", e.getErrMsg());
            return true;
        }

        //消息是否消费锁标识
        String consumerLock = String.format(RedisKey.EXTERNAL_MQ_LOCK_TENPAY_REFUND, orderId);
        //判断这个订单是否正在被消费
        RLock lock = cacheManager.redissonClient().getLock(consumerLock);
        if (lock.isLocked()) {
            logger.info(">> mq 已经被消费 orderId : {} ", orderId);
            return true;
        }
        String payOrderId = refundOrder.getPayOrderId();
        ExternalPaymentOrder paymentOrder = externalPaymentOrderRepository.findOneByOrderId(payOrderId);

        ResponsePacket packet = null;
        Boolean flag = true;

        try {
            lock.lock(3, TimeUnit.SECONDS);
            //向第三方发送退款请求
            Map<String, String> resultMap = TenpayUtil.createRefundInfo(refundOrder, paymentOrder, tenpaySetting, channelTag, appId);
            String respBody = resultMap.get("requestParams");
            String respStr = resultMap.get("responseParams");

            refundOrder = TenpayUtil.checkRefundReponse(respStr, tenpaySetting, refundOrder);

            externalRefundUtil.refreshRefundInfo(refundOrder);

            //更新MongoDB中的退款信息
            RefundVoucher refundVoucher = refundVoucherRepository.findOneByOrderId(orderId);
            if (refundVoucher == null) {
                refundVoucher = new RefundVoucher();
                refundVoucher.setOrderId(orderId);
            }
            refundVoucher.setState(refundOrder.getState());
            refundVoucher.setRequestParams(respBody);
            refundVoucher.setRequestMethod(respStr);
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
     * @Description: 处理支付回调信息
     * @Param: [xmlStr]
     * @Return java.lang.Boolean
     * @Author: xusisi
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean handlePaymentNotify(String xmlStr, String appId, String channelTag) {

        Map<String, String> responseMap = PayXmlutils.xmlTomap(xmlStr);

        //校验订单信息，校验金额信息
        String respOutTradeNo = responseMap.get(TenpayConstants.OUT_TRADE_NO);

        ExternalPaymentOrder paymentOrder = externalPaymentOrderRepository.findOneByOrderId(respOutTradeNo);
        if (paymentOrder == null) {
            logger.warn("支付订单不存在，不继续下一步操作");
            return false;
        }
        //订单总金额，单位为分
        String respAmount = responseMap.get(TenpayConstants.TOTAL_FEE);
        if (!Integer.valueOf(respAmount).equals(paymentOrder.getTotalFee())) {
            logger.warn("订单金额不一致，不继续下一步操作");
            return false;
        }
        //订单状态不是待支付，不需要在处理
        String state = paymentOrder.getState();
        if (!ExternalPayStateEnum.UNPAIED.getCode().equals(state)) {
            logger.warn("订单状态不是待支付，不需要再处理，不继续下一步");
            return true;
        }

        String respState = responseMap.get(TenpayConstants.RESULT_CODE);
        if (TenpayConstants.RESULT_CODE_FAIL.equals(respState)) {
            logger.warn("支付失败时，还是可以继续支付的，所以订单的状态还是待支付");
            return true;
        }

        //保存回调信息到MongoDB中
        PaymentVoucher paymentVoucher = paymentVoucherRepository.findOneByOrderId(respOutTradeNo);
        if (paymentVoucher == null) {
            paymentVoucher = new PaymentVoucher();
            paymentVoucher.setOrderId(respOutTradeNo);
        }

        paymentVoucher.setMetadata(responseMap);
        //更新凭证数据信息
        paymentVoucherRepository.save(paymentVoucher);
        //发送MQ消息
        ExternalPayOrderMQ externalPayOrderMQ = new ExternalPayOrderMQ();
        externalPayOrderMQ.setOrderId(respOutTradeNo);
        externalPayOrderMQ.setAppId(appId);
        externalPayOrderMQ.setChannelTag(channelTag);
        externalPayOrderMQ.setChannelType(ExternalOrderPayChannelType.TENPAY.getValue());

        Message message =
                MessageBuilder.of(externalPayOrderMQ).topic(RocketMQConstant.PAYMENT_TOPIC).tag(RocketMQConstant.EXTERNAL_TENPAY_PAY_NOTIFY_TAG).build();
        message.setTransactionId(CommonUtils.genTransactionId(RocketMQConstant.PAYMENT_TRANSACTION_ID, respOutTradeNo));

        logger.debug("sendMessage >> start >> topic :{} , tag :{} , message : {} , externalPayOrderMQ : {} ",
                RocketMQConstant.PAYMENT_TOPIC,
                RocketMQConstant.EXTERNAL_TENPAY_PAY_NOTIFY_TAG,
                message,
                externalPayOrderMQ);

        tenpayPayNotifyProductor.sendMessageInTransaction(message, externalPayOrderMQ);

        logger.debug("sendMessage >> end >> topic :{} , tag :{} , message : {}  externalPayOrderMQ : {} ",
                RocketMQConstant.PAYMENT_TOPIC,
                RocketMQConstant.EXTERNAL_TENPAY_PAY_NOTIFY_TAG, message,
                externalPayOrderMQ);

        return true;
    }

    /**
     * @Description: 处理退款回调信息
     * @Param: [xmlStr, appId, channelTag, tenpaySetting]
     * @Return java.lang.Boolean
     * @Author: xusisi
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean handleRefundNotify(String xmlStr, String appId, String channelTag, TenpaySetting tenpaySetting) {

        //Map<String, String> responseMap = PaySignUtils.processResponseXml(xmlStr, tenpaySetting);

        String decryptDataXML = null;
        try {
            decryptDataXML = PaySignUtils.decryptData(xmlStr, tenpaySetting.getApiSecret());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("微信退款回调解密失败");
            return false;
        }
        logger.info("腾讯退款回调原始凭据：{}", decryptDataXML);
        Map<String, String> refundMap = PayXmlutils.xmlTomap(decryptDataXML);

        String respOutRefundNo = refundMap.get(TenpayConstants.OUT_REFUND_NO);
        ExternalRefundOrder refundOrder = externalRefundOrderRepository.findOneByOrderId(respOutRefundNo);
        if (refundOrder == null) {
            logger.error("退款回调异常-退款订单并不存在");
            return false;
        }
        String refundStatus = refundOrder.getState();
        if (!ExternalRefundStateEnum.REFUNDING.getCode().equals(refundStatus)) {
            logger.info("微信-退款回调已经处理，返回成功");
            return true;
        }

        //更新MongoDB中的退款订单信息
        RefundVoucher refundVoucher = refundVoucherRepository.findOneByOrderId(respOutRefundNo);
        if (refundVoucher == null) {
            refundVoucher = new RefundVoucher();
            refundVoucher.setOrderId(respOutRefundNo);
        }
        refundVoucher.setMetadata(refundMap);
        refundVoucherRepository.save(refundVoucher);

        //发送MQ消息
        ExternalRefundOrderMQ externalRefundOrderMQ = new ExternalRefundOrderMQ();
        externalRefundOrderMQ.setOrderId(respOutRefundNo);
        externalRefundOrderMQ.setChannelTag(channelTag);
        externalRefundOrderMQ.setAppId(appId);
        externalRefundOrderMQ.setChannelType(ExternalOrderPayChannelType.TENPAY.getValue());

        Message message =
                MessageBuilder.of(externalRefundOrderMQ).topic(RocketMQConstant.PAYMENT_TOPIC).tag(RocketMQConstant.EXTERNAL_TENPAY_REFUND_NOTIFY_TAG).build();
        message.setTransactionId(CommonUtils.genTransactionId(RocketMQConstant.PAYMENT_TRANSACTION_ID, respOutRefundNo));

        logger.debug("sendMessage >> start >> topic :{} , tag :{} , message : {} , externalRefundOrderMQ : {} ",
                RocketMQConstant.PAYMENT_TOPIC,
                RocketMQConstant.EXTERNAL_TENPAY_REFUND_NOTIFY_TAG,
                message,
                externalRefundOrderMQ);

        tenpayRefundNotifyProductor.sendMessageInTransaction(message, externalRefundOrderMQ);

        logger.debug("sendMessage >> end >> topic :{} , tag :{} , message : {}  externalRefundOrderMQ : {} ",
                RocketMQConstant.PAYMENT_TOPIC,
                RocketMQConstant.EXTERNAL_TENPAY_REFUND_NOTIFY_TAG, message,
                externalRefundOrderMQ);

        return true;

    }

    @Override
    public ExternalPaymentOrder cancelPaymentOrder(CloseOrCancelPayOrderParams closeOrCancelPayOrderParams, String appId, String channelTag,
                                                   AbstractEntity paySetting) throws BusinessException {
        return null;
    }

    @Override
    public CloudAccountRespVo createWithdrawOrder(CloudAccountOrderParams orderParams, String appId, String channelTag, AbstractEntity paySetting,
                                                  String ip) throws BusinessException {
        return null;
    }

    @Override
    public void walletNotify(Map<String, String> params) {

    }

}

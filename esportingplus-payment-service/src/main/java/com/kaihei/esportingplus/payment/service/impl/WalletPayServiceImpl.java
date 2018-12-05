package com.kaihei.esportingplus.payment.service.impl;

import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.payment.api.params.*;
import com.kaihei.esportingplus.payment.api.vo.CloudAccountRespVo;
import com.kaihei.esportingplus.payment.api.vo.CreateRefundOrderReturnVo;
import com.kaihei.esportingplus.payment.api.vo.GCoinPaymentPreVo;
import com.kaihei.esportingplus.payment.mq.message.MQSimpleOrder;
import com.kaihei.esportingplus.payment.mq.message.WalletPayNotifyMQ;
import com.kaihei.esportingplus.payment.domain.entity.AbstractEntity;
import com.kaihei.esportingplus.payment.domain.entity.ExternalPaymentOrder;
import com.kaihei.esportingplus.payment.domain.entity.ExternalRefundOrder;
import com.kaihei.esportingplus.payment.domain.entity.GCoinPaymentOrder;
import com.kaihei.esportingplus.payment.mq.producer.WalletPayNotifyProducer;
import com.kaihei.esportingplus.payment.mq.producer.WalletPayProducer;
import com.kaihei.esportingplus.payment.service.GCoinPaymentService;
import com.kaihei.esportingplus.payment.service.PayService;
import com.kaihei.esportingplus.payment.util.CommonUtils;
import com.maihaoche.starter.mq.base.MessageBuilder;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: esportingplus
 * @description: 内部支付-暴鸡币支付
 * @author: xusisi
 * @create: 2018-11-14 16:08
 **/
@Service("walletPayService")
public class WalletPayServiceImpl implements PayService {

    private final Logger logger = LoggerFactory.getLogger(WalletPayServiceImpl.class);

    @Autowired
    private GCoinPaymentService gCoinPaymentService;

    @Autowired
    private WalletPayProducer walletPayProducer;

    @Autowired
    private WalletPayNotifyProducer walletPayNotifyProducer;

    @Override
    public Map<String, String> createPaymentOrder(PayOrderParams payOrderParams, String appId, String channelTag, AbstractEntity paySetting,
                                                  String ip, String area) throws BusinessException {

        logger.debug("暴鸡币支付>>>");

        String userId = payOrderParams.getUserId();
        String amount = String.valueOf(payOrderParams.getTotalAmount());
        String orderType = payOrderParams.getOrderType();
        String outTradeNo = payOrderParams.getOutTradeNo();
        String subject = payOrderParams.getSubject();
        String body = payOrderParams.getBody();
        String description = payOrderParams.getDescription();
        String sourceId = CommonUtils.getSourceId(appId);
        String notifyUrl = payOrderParams.getNotifyUrl();

        //重新封装数据请求
        GCoinPaymentCreateParams createParams = new GCoinPaymentCreateParams();
        createParams.setAmount(amount);
        createParams.setUserId(userId);
        createParams.setOrderType(orderType);
        createParams.setOutTradeNo(outTradeNo);
        createParams.setSubject(subject);
        createParams.setBody(body);
        createParams.setDescription(description);
        createParams.setSourceId(sourceId);
        //保存业务方的回调地址
        createParams.setAttach(notifyUrl);

        logger.debug("GCoinPaymentCreateParams : {} ", createParams);
        GCoinPaymentPreVo vo = gCoinPaymentService.preCreatePayment(createParams);
        logger.debug("GCoinPaymentPreVo : {} ", vo);
        String orderId = vo.getOrderId();
        Map<String, String> map = new HashMap<>();
        map.put("order_id", vo.getOrderId());
        logger.info("出参 >>>> map : {} ", map);

        //开启异步操作更新暴鸡币支付信息
        //提交异步消息到MQ
        try {

            MQSimpleOrder mq = new MQSimpleOrder();
            mq.setOutTradeNo(outTradeNo);
            mq.setUserId(userId);
            //暴鸡币支付成功的回调地址
            String gcoinNotifyUrl = "http://127.0.0.1:8181/v3/generator/payment/trade/payment_notify/" + appId + "/" + channelTag;
            mq.setNotifyUrl(gcoinNotifyUrl);
            mq.setOrderId(orderId);

            Message message = MessageBuilder.of(mq).topic(RocketMQConstant.PAYMENT_TOPIC).tag(RocketMQConstant.EXTERNAL_GCOIN_PAY_TAG).build();
            message.setTransactionId(CommonUtils.genTransactionId(RocketMQConstant.PAYMENT_TRANSACTION_ID, orderId));

            logger.debug("sendMessage >> start >> topic :{} , tag :{} , message : {} , MQSimpleOrder : {} ",
                    RocketMQConstant.PAYMENT_TOPIC,
                    RocketMQConstant.EXTERNAL_GCOIN_PAY_TAG,
                    message,
                    mq);
            walletPayProducer.sendMessageInTransaction(message, mq);

            logger.debug("sendMessage >> end >> topic :{} , tag :{} , message : {}  MQSimpleOrder : {} ",
                    RocketMQConstant.PAYMENT_TOPIC,
                    RocketMQConstant.EXTERNAL_GCOIN_PAY_TAG,
                    message,
                    mq);

        } catch (Exception e) {
            logger.error("updatePaymentInfo >> exception :{} ", e.getMessage());
            throw new BusinessException(BizExceptionEnum.ROCKETMQ_PRODUCER_ERROR);
        }

        return map;
    }

    @Override
    public ExternalPaymentOrder searchPaymentOrderInfo(String appId, String channelTag, String outTradeNo, AbstractEntity paySetting,
                                                       String orderType) throws BusinessException {
        return null;
    }

    @Override
    public CreateRefundOrderReturnVo createRefundOrder(RefundOrderParams refundOrderParams, String appId, String channelTag,
                                                       AbstractEntity paySetting) throws BusinessException {
        return null;
    }

    @Override
    public ExternalRefundOrder searchRefundOrderInfo(String appId, String channelTag, String outRefundNo, AbstractEntity paySetting) throws BusinessException {
        return null;
    }

    @Override
    public ExternalPaymentOrder closePaymentOrder(CloseOrCancelPayOrderParams closeOrCancelPayOrderParams, String appId, String channelTag,
                                                  AbstractEntity paySetting) throws BusinessException {
        return null;
    }

    @Override
    public ExternalPaymentOrder cancelPaymentOrder(CloseOrCancelPayOrderParams closeOrCancelPayOrderParams, String appId, String channelTag,
                                                   AbstractEntity paySetting) throws BusinessException {
        return null;
    }

    @Override
    public Boolean handleNotify(Map<String, String> params, String appId, String channelTag, AbstractEntity paySetting, String type) throws BusinessException {
        return null;
    }

    @Override
    public CloudAccountRespVo createWithdrawOrder(CloudAccountOrderParams orderParams, String appId, String channelTag, AbstractEntity paySetting,
                                                  String ip) throws BusinessException {
        return null;
    }

    @Override
    public void walletNotify(Map<String, String> params) {

        //{msg=暴鸡币支付，该业务订单已支付完成 , code=120403, data={"orderId":"261184939558703104","subject":"","description":"","body":""}}
        String code = params.get("code");
        String msg = params.get("msg");
        String data = params.get("data");
        logger.debug("data >> {} ", data);
        //暴鸡币支付订单
        GCoinPaymentOrder order = FastJsonUtils.fromJson(data, GCoinPaymentOrder.class);
        logger.debug("order >> {} ", order);
        //读取对应的信息，将信息保存至MQ，
        try {
            String orderId = order.getOrderId();
            WalletPayNotifyMQ mq = new WalletPayNotifyMQ();
            mq.setOrderId(orderId);
            mq.setCode(code);
            mq.setMsg(msg);

            Message message = MessageBuilder.of(mq).topic(RocketMQConstant.PAYMENT_TOPIC).tag(RocketMQConstant.EXTERNAL_GCOIN_NOTIFY_TAG).build();
            message.setTransactionId(CommonUtils.genTransactionId(RocketMQConstant.PAYMENT_TRANSACTION_ID, orderId));

            logger.debug("sendMessage >> start >> topic :{} , tag :{} , message : {} , WalletPayNotifyMQ : {} ",
                    RocketMQConstant.PAYMENT_TOPIC,
                    RocketMQConstant.EXTERNAL_GCOIN_NOTIFY_TAG,
                    message,
                    mq);
            walletPayNotifyProducer.sendMessageInTransaction(message, mq);

            logger.debug("sendMessage >> end >> topic :{} , tag :{} , message : {}  WalletPayNotifyMQ : {} ",
                    RocketMQConstant.PAYMENT_TOPIC,
                    RocketMQConstant.EXTERNAL_GCOIN_NOTIFY_TAG,
                    message,
                    mq);

        } catch (Exception e) {
            logger.error("updatePaymentInfo >> exception :{} ", e.getMessage());
            throw new BusinessException(BizExceptionEnum.ROCKETMQ_PRODUCER_ERROR);

        }

    }

}

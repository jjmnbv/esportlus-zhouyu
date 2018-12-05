package com.kaihei.esportingplus.trade.mq.consumer;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.payment.api.enums.PayChannelEnum;
import com.kaihei.esportingplus.payment.api.feign.TradeServiceClient;
import com.kaihei.esportingplus.payment.api.params.RefundOrderParams;
import com.kaihei.esportingplus.trade.data.repository.OrderRefundRecordRepositry;
import com.kaihei.esportingplus.trade.data.repository.OrderRepository;
import com.kaihei.esportingplus.trade.domain.entity.Order;
import com.kaihei.esportingplus.trade.mq.message.RefundMessage;
import com.maihaoche.starter.mq.annotation.MQConsumer;
import com.maihaoche.starter.mq.base.AbstractMQPushConsumer;
import java.util.Date;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@MQConsumer(topic = RocketMQConstant.TOPIC_PVP, tag = RocketMQConstant.REFUND_ORDER_TAGS,
        consumerGroup = RocketMQConstant.REFUND_ORDER_CONSUMER_PVP_GROUP)
public class PVPRefundOrderConsumer extends AbstractMQPushConsumer<RefundMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PVPRefundOrderConsumer.class);

    @Autowired
    private TradeServiceClient tradeServiceClient;

    @Autowired
    private OrderRepository orderRepository;

    //订单类型:17=DNF订单
    @Value("${pay.refund.param.order_type}")
    private int order_type;
    //回调通知地址
    @Value("${pay.refund.param.notify_url}")
    private String notify_url;
    //重试次数
    @Value("${refund.mq.retryTimes:5}")
    private int refundRetry;

    @Autowired
    protected OrderRefundRecordRepositry orderRefundRecordRepositry;

    private CacheManager cacheManager = CacheManagerFactory.create();

    @Override
    public boolean process(RefundMessage refundMessage, Map map) {
        int reconsume_times = (int) map.get("RECONSUME_TIMES");
        //校验消息为空
        Object msg_id = map.get("MSG_ID");

        if(refundMessage == null || refundMessage.getOrder() == null){
            LOGGER.info(">>退款请求消息为空：{}，MQ消息id:{}，不做处理",
                    refundMessage,
                    msg_id);
            return true;
        }

        Order order = refundMessage.getOrder();
        String sequeue = order.getSequeue();

        ///消息重复消费处理：消费一次缓存起来 如果存在说明此消息发生过消费，直接return true;
        Boolean repeatConsume = cacheManager.exists(RedisKey.REFUND_HISTORY+sequeue);
        if(repeatConsume != null && repeatConsume){
            LOGGER.info(">>消息重复消费，订单[{}]已经发起过退款，MQ消息id:{}，忽略处理",
                    sequeue,
                    msg_id);
            return true;
        }

        try {

            //开始退款
            RefundOrderParams refundParams = new RefundOrderParams();
            refundParams.setRefundAmount(refundMessage.getRefundFee()+"");
            refundParams.setOutTradeNo(order.getSequeue());
            //TODO 这东西配死？
            refundParams.setOrderType(order_type+"");
            refundParams.setUserId(order.getUid());
            refundParams.setOutRefundNo(refundMessage.getRefundSequence());
            refundParams.setSubject("付费车队订单退款-"+PayChannelEnum.lookup(order.getPaymentType()).getChannelName());
            //业务单号，对于支付系统，我们属于第三方系统
            refundParams.setNotifyUrl(notify_url);

            ResponsePacket responsePacket = tradeServiceClient.refund(refundParams);
            if (responsePacket.getCode() == BizExceptionEnum.HYSTRIX_SERVER.getErrCode()) {
                LOGGER.error(responsePacket.toString());
                LOGGER.error("发起退款请求失败，MQ消息id:{},订单号: {},重试：{}",
                        msg_id,
                        sequeue,
                        reconsume_times + 1);
                if (reconsume_times >= refundRetry - 1) {
                    LOGGER.error(">>发起退款请求失败,MQ消息id:{},订单号: {},重试次数已达阀值:{},不在处理",
                            msg_id,
                            sequeue,
                            refundRetry);
                    return true;
                }
                return false;
            } else{
                LOGGER.info("发起退款成功，MQ消息id:{},开始更新订单[{}]预退款金额[{}],结果:{}"
                        , msg_id,order.getSequeue(), refundMessage.getRefundFee(),responsePacket.toString());
                Order orderParam = new Order();
                orderParam.setSequeue(order.getSequeue());

                orderParam.setPreRefundAmount(refundMessage.getRefundFee());
                orderParam.setCancelTime(new Date());
                orderRepository.updateSelectiveBySequenceId(orderParam);
                LOGGER.error("订单更新完毕，MQ消息id:{},订单号: {}",msg_id,sequeue);

                //2 分钟失效：mq默认重试5次 1/5/10/30/60,将近2分钟。
                cacheManager.del(RedisKey.REFUND_HISTORY + sequeue);

                return true;
            }
        } catch (Exception e) {
            LOGGER.error(">>发起退款请求失败，MQ消息id:{},订单号: {},重试: {}",
                    msg_id,
                    sequeue,
                    reconsume_times + 1,e);
            if (reconsume_times >= refundRetry - 1) {
                LOGGER.error(">>发起退款请求失败,MQ消息id:{},订单号: {},重试次数已达阀值:{},不在处理",
                        msg_id,
                        sequeue,
                        refundRetry);
                return true;
            }
            return false;
        }

    }
}

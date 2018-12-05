package com.kaihei.esportingplus.trade.mq.consumer;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.trade.api.enums.TeamOrderRPGActionEnum;
import com.kaihei.esportingplus.trade.api.params.RefundParams;
import com.kaihei.esportingplus.trade.data.repository.OrderRepository;
import com.kaihei.esportingplus.trade.domain.entity.Order;
import com.kaihei.esportingplus.trade.mq.message.RefundMessage;
import com.kaihei.esportingplus.trade.data.manager.PythonRestClient;
import com.maihaoche.starter.mq.annotation.MQConsumer;
import com.maihaoche.starter.mq.base.AbstractMQPushConsumer;
import java.util.Date;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@MQConsumer(topic = RocketMQConstant.TOPIC_RPG, tag = RocketMQConstant.REFUND_ORDER_TAGS,
        consumerGroup = RocketMQConstant.REFUND_ORDER_CONSUMER_RPG_GROUP)
public class RPGRefundOrderConsumer extends AbstractMQPushConsumer<RefundMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RPGRefundOrderConsumer.class);

    @Autowired
    private PythonRestClient pythonRestClient;

    @Autowired
    private OrderRepository orderRepository;

    //退款类型（是否需要审核）: 1免审核，2需要审核
    @Value("${pay.refund.param.refund_type}")
    private int refund_type;
    //订单类型:17
    @Value("${pay.refund.param.order_type}")
    private int order_type;
    //退款渠道:1暴鸡钱包2微信4支付宝7QQ钱包
    @Value("${pay.refund.param.channel}")
    private int channel;
    //回调通知地址
    @Value("${pay.refund.param.notify_url}")
    private String notify_url;
    //包名 1 APP kaihei 2 小程序 miniprogram
    @Value("${pay.refund.param.package_name}")
    private String package_name;

    @Value("${python.refund.retryTimes:5}")
    private int refundRetry;

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
            RefundParams refundParams = new RefundParams();
            refundParams.setRefund_type(refund_type);
            refundParams.setOrder_type(order_type);
            refundParams.setChannel(channel);
            refundParams.setNotify_url(notify_url);
            refundParams.setPackage_name(package_name);
            refundParams.setUid(order.getUid());
            refundParams.setOrder_id(sequeue);
            refundParams.setFee(order.getPreRefundAmount());
            refundParams.setRemark(TeamOrderRPGActionEnum.fromCode(refundMessage.getMemberStatus()).getDesc());

            String refundJson = FastJsonUtils.toJson(refundParams);

            ResponsePacket responsePacket = pythonRestClient.refund(refundJson);
            if (responsePacket.getCode() != BizExceptionEnum.SUCCESS.getErrCode()) {
                LOGGER.error(responsePacket.getMsg());
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
            } else {
                LOGGER.info("发起退款成功，MQ消息id:{},开始更新订单[{}]预退款金额[{}]"
                        , msg_id,refundParams.getOrder_id(), refundParams.getFee());
                Order orderParam = new Order();
                orderParam.setSequeue(refundParams.getOrder_id());
                orderParam.setPreRefundAmount(refundParams.getFee());
                orderParam.setCancelTime(new Date());
                orderParam.setGmtModified(new Date());
                orderRepository.updateSelectiveBySequenceId(orderParam);
                LOGGER.error("订单更新完毕，MQ消息id:{},订单号: {}",msg_id,sequeue);

                //2 分钟失效：mq默认重试5次 1/5/10/30/60,将近2分钟。
                cacheManager.set(RedisKey.REFUND_HISTORY + sequeue, StringUtils.EMPTY,120);
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

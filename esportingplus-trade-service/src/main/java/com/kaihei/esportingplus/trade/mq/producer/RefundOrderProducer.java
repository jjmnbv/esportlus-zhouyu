package com.kaihei.esportingplus.trade.mq.producer;

import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.tools.ApplicationContextUtil;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.trade.common.UpdateOrderParams;
import com.kaihei.esportingplus.trade.domain.entity.Order;
import com.kaihei.esportingplus.trade.domain.entity.OrderRefundRecord;
import com.kaihei.esportingplus.trade.domain.service.OrderService;
import com.kaihei.esportingplus.trade.domain.service.PVPOrderService;
import com.kaihei.esportingplus.trade.domain.service.RPGOrderService;
import com.kaihei.esportingplus.trade.enums.GameOrderType;
import com.kaihei.esportingplus.trade.mq.message.RefundMessage;
import com.maihaoche.starter.mq.annotation.MQTransactionProducer;
import com.maihaoche.starter.mq.base.AbstractMQTransactionProducer;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *@Description: 本地事物更新老板订单状态
 *
 *@author  Orochi-Yzh
 *@dateTime  2018/11/8 11:03
*/
@MQTransactionProducer(producerGroup = RocketMQConstant.REFUND_ORDER_PRODUCER_RPG_GROUP)
public class RefundOrderProducer extends AbstractMQTransactionProducer{

    private static final Logger LOGGER = LoggerFactory.getLogger(RefundOrderProducer.class);

    /**
     * 本地事务方法
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object orderType) {
        String transactionId = msg.getTransactionId();

        RefundMessage refundMessage = FastJsonUtils
                .fromJson(msg.getBody(), RefundMessage.class);

        if(refundMessage == null){
            LOGGER.info(">> 发送退款消息到MQ失败,消息体为空");
        }else{
            Order order = refundMessage.getOrder();
            // 提交本地事务
            LOGGER.info(">> 发送退款消息到MQ成功,订单号:[{}],消息id:[{}]",
                    order.getSequeue(), transactionId);
            try {
                //更新老板订单状态
                //历史退款总额
                int refundRecordSum = order.getOrderRefundRecords().stream()
                        .mapToInt(OrderRefundRecord::getRefundFee).sum();
                //当前退款金额
                int currentRefundAmout = refundMessage.getRefundFee();
                //如果历史退款总额+当前退款金额比实付金额大说明 退款超额了，忽略处理
                if(refundRecordSum + currentRefundAmout > order.getActualPaidAmount()){
                    LOGGER.error("此次退款金额[{}]+历史退款总额[{}] > 实付金额[{}]，忽略处理,订单号:[{}],消息id:[{}]",
                            currentRefundAmout,refundRecordSum,order.getActualPaidAmount(),order.getSequeue(), transactionId);
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
                //设置预退款金额
                order.setPreRefundAmount(refundMessage.getRefundFee());
                UpdateOrderParams params = UpdateOrderParams.builder()
                        .order(order)
                        .memberStatus(refundMessage.getMemberStatus())
                        .refundSequence(refundMessage.getRefundSequence())
                        .neddRefund(true)
                        .build();

                GameOrderType orderTypeEnum = (GameOrderType) orderType;
                OrderService orderService;
                if(orderTypeEnum.equals(GameOrderType.RPG)){
                    orderService = ApplicationContextUtil.getBean(RPGOrderService.class);
                }else{
                    orderService = ApplicationContextUtil.getBean(PVPOrderService.class);
                }
                Order updatedOrder = orderService.updateOrder(params);
                if (updatedOrder != null) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
            } catch (Exception e) {
                LOGGER.error(">> 发送退款消息到MQ失败,订单号:[{}],消息id:[{}]",
                        order.getSequeue(), transactionId,e);
            }
        }

        return LocalTransactionState.ROLLBACK_MESSAGE;
    }

    /**
     * 本地事务确认
     *
     * executeLocalTransaction返回UNKNOW 的情况下 MQ会在一段时间后调用producerGroup相同的应用
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        return LocalTransactionState.ROLLBACK_MESSAGE;
    }

}
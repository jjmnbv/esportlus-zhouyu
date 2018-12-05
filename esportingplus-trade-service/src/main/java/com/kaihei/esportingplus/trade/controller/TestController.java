package com.kaihei.esportingplus.trade.controller;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.algorithm.SnowFlake;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.tools.DateUtil;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.gamingteam.api.mq.PVPFreeTeamEndOrderMessage;
import com.kaihei.esportingplus.payment.api.params.OrderIncomeParams;
import com.kaihei.esportingplus.trade.common.TradeConstants;
import com.kaihei.esportingplus.trade.domain.entity.Order;
import com.kaihei.esportingplus.trade.domain.service.PVPFreeOrderService;
import com.kaihei.esportingplus.trade.domain.service.PVPOrderService;
import com.kaihei.esportingplus.trade.enums.GameOrderType;
import com.kaihei.esportingplus.trade.mq.message.RefundMessage;
import com.kaihei.esportingplus.trade.mq.producer.RefundOrderProducer;
import com.kaihei.esportingplus.user.api.enums.UserPointItemType;
import com.kaihei.esportingplus.user.api.feign.UserPointServiceClient;
import com.maihaoche.starter.mq.base.MessageBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单
 *
 * @author liangyi
 */
@RestController
@RequestMapping("/test")
@Api(tags = {"test"})
@RefreshScope
public class TestController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private CacheManager cacheManager = CacheManagerFactory.create();

    @Value("${spring.rocketmq.name-server-address}")
    private String mqNameServer;

    @Value("${swagger.api-info.contact.name}")
    private String swaggerContactName;

    @Autowired
    private PVPFreeOrderService pvpFreeOrderService;

    @Autowired
    private PVPOrderService pvpOrderService;

    @Autowired
    private UserPointServiceClient userPointServiceClient;

    @GetMapping("/{uid}")
    public ResponsePacket<String> testString(@PathVariable String uid) throws InterruptedException {
        Thread.sleep(2000);
        System.out.println(swaggerContactName);
        return ResponsePacket.onSuccess(uid);
    }

    @GetMapping("/redis/{uid}")
    public ResponsePacket<UserSessionContext> testRedis(@RequestHeader("Authorization") String token, @PathVariable String uid){
        System.out.println(swaggerContactName);
        return ResponsePacket.onSuccess(UserSessionContext.getUser());
    }

    @ApiOperation(value = "测试MQ订单退款")
    @GetMapping("/testRefund/order/{sequence}")
    public ResponsePacket<Void> refundOrder(@PathVariable("sequence") String sequence){
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("test_producer_group");
        defaultMQProducer.setNamesrvAddr(mqNameServer);

        try {
            defaultMQProducer.start();

            Message message = MessageBuilder
                    .of(sequence)
                    .topic(RocketMQConstant.TOPIC_RPG)
                    .tag(RocketMQConstant.REFUND_ORDER_TAGS_FROM_TEAM)
                    .build();

            SendResult sendResult = defaultMQProducer.send(message);
            System.out.println("发送消息结果, msgId:" + sendResult.getMsgId() +
                    ", 发送状态:" + sendResult.getSendStatus());

        } catch (MQClientException | InterruptedException
                | RemotingException | MQBrokerException e) {
            e.printStackTrace();
        } finally {
            defaultMQProducer.shutdown();
        }

        return ResponsePacket.onSuccess();
    }

    @Autowired
    private RefundOrderProducer refundOrderProducer;
    @Autowired
    protected SnowFlake snowFlake;

    @ApiOperation(value = "测试MQ订单退款2")
    @GetMapping("/testRefund2/order/{sequence}")
    public ResponsePacket<Void> refundOrder2(){

        String refundSequence = TradeConstants.ORDER_ID_PREFIEX
                + DateUtil.nowDateTime(DateUtil.SIMPLE_FORMATTER)
                + snowFlake.nextId();

        RefundMessage refundMessage = new RefundMessage();
        refundMessage.setMemberStatus(8);
        refundMessage.setRefundSequence(refundSequence);
        refundMessage.setRefundFee(2);
        Order order = pvpOrderService
                .getBySequenceId("BJ201811202202262379126583984128");
        refundMessage.setOrder(order);
        String msgBody = FastJsonUtils.toJson(refundMessage);
        Message message = new Message();
        message.setTopic(RocketMQConstant.TOPIC_PVP);
        message.setTags(RocketMQConstant.REFUND_ORDER_TAGS);
        message.setBody(msgBody.getBytes());
        SendResult sendResult = refundOrderProducer
                .sendMessageInTransaction(message, GameOrderType.RPG);
        if (ObjectTools.isNotNull(sendResult)
                && SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
            return ResponsePacket.onSuccess();
        }
        // 直接抛出业务异常
        LOGGER.error(" >> 发送退款消息错误! tags: {}, teamSequence: {}, msgBody: {}",
                RocketMQConstant.REFUND_ORDER_TAGS, msgBody);
        return ResponsePacket.onError();
    }

    @GetMapping("/sendProfit")
    public ResponsePacket<Void> sendProfit(){
        OrderIncomeParams orderIncomeParams = new OrderIncomeParams();
        orderIncomeParams.setOrderId("BJ201810261838253268242569887744");
        orderIncomeParams.setUserId("2a13700e");
        orderIncomeParams.setOrderType(17);
        orderIncomeParams.setAmount(10);
//        EventBus.post(new SendProfitEvent(orderIncomeParams));

        return userPointServiceClient.incrPoint("01eb8f17",40,
                UserPointItemType.TEAM_DRIVE.getCode(), "GT262659622479986688");
    }

    @ApiOperation(value = "updateOrderAndSendInCome")
    @PostMapping("/updateOrderAndSendInCome")
    public ResponsePacket updateOrderAndSendInCome(@RequestBody PVPFreeTeamEndOrderMessage message){

        pvpFreeOrderService.updateOrderAndSendInCome(message);
        return ResponsePacket.onSuccess();
    }

}

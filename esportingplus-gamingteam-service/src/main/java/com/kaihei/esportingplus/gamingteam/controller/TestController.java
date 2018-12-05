package com.kaihei.esportingplus.gamingteam.controller;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.gamingteam.api.mq.PVPFreeTeamEndOrderMessage;
import com.maihaoche.starter.mq.base.MessageBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
public class TestController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private CacheManager cacheManager = CacheManagerFactory.create();

    @Value("${spring.rocketmq.name-server-address}")
    private String mqNameServer;

    @ApiOperation(value = "testInComeMQ")
    @PostMapping("/testInComeMQ")
    public ResponsePacket<Void> testInComeMQ(@RequestBody PVPFreeTeamEndOrderMessage msg){
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("testInCome");
        defaultMQProducer.setNamesrvAddr(mqNameServer);

        try {
            defaultMQProducer.start();

            Message message = MessageBuilder
                    .of(msg)
                    .topic(RocketMQConstant.TOPIC_PVP_FREE)
                    .tag(RocketMQConstant.UPDATE_ORDER_STATUS_TAG)
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

}

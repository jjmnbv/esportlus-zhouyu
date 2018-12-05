package com.kaihei.esportingplus.payment.listener;

import com.kaihei.commons.cache.redis.mq.ListenerDispatcher;
import com.kaihei.esportingplus.payment.api.enums.RedisKeyType;
import com.kaihei.esportingplus.payment.config.RedisConfig;
import com.kaihei.esportingplus.payment.service.GCoinPaymentService;
import com.kaihei.esportingplus.payment.service.GCoinRechargeService;
import com.kaihei.esportingplus.payment.service.GCoinRefundService;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * Redis KEY 失效监听并处理
 *
 * @author xusisi, haycco
 **/
@Component
@ConditionalOnBean(RedisConfig.class)
public class RedisKeyExpiredListener {

    private static final Logger logger = LoggerFactory.getLogger(RedisKeyExpiredListener.class);

    @Value("${redis.database:0}")
    private int database;

    @Autowired
    private GCoinRechargeService gCoinRechargeService;

    @Autowired
    private GCoinRefundService gCoinRefundService;

    @Autowired
    private GCoinPaymentService gCoinPaymentService;

    @PostConstruct
    public void afterPropertiesSet() {

        ListenerDispatcher.getInstance().subscribe("__keyevent@" + database + "__:expired",
                (String topic, Object message) -> {

                    logger.debug("redis topic : {} >> msg :{} ", topic, message);
                    //gcoin:payment:key:67
                    String messageStr = message.toString();
                    String[] arrs = messageStr.split(":");
                    //该失效的key不符合要处理的要求，直接忽略
                    if (arrs == null || arrs.length != 4) {
                        return;
                    }

                    String title = arrs[0];
                    String business = arrs[1];
                    String userId = arrs[2];
                    String orderId = arrs[3];

                    if (RedisKeyType.RECHARGE.getCode().equals(business)) {
                        logger.debug("处理充值超时操作：userId : {} ,orderId :{} ", userId, orderId);
                        gCoinRechargeService.refreshOrderInfo(orderId, userId);

                    } else if (RedisKeyType.PAYMENT1.getCode().equals(business)) {

                        logger.debug("处理支付超时操作：userId : {} ,orderId : {} ", userId, orderId);
                        gCoinPaymentService.refreshPaymentInfo(orderId, userId);

                    } else if (RedisKeyType.REFUND.getCode().equals(business)) {
                        logger.debug("处理退款超时操作: userId : {} ,orderId : {} ", userId, orderId);
                        gCoinRefundService.refreshRefundOrder(orderId, userId);

                    }
                });
    }
}

package com.kaihei.esportingplus.trade.event;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.kaihei.esportingplus.common.event.EventConsumer;
import com.kaihei.esportingplus.trade.domain.service.RPGOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PythonRefundNofityEventConsumer extends EventConsumer {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private RPGOrderService rpgOrderService;

    /**
     * 退款回调通知
     * @param event
     */
    @Subscribe
    @AllowConcurrentEvents //开启线程安全
    public void updateOrder(PythonRefundNofityEvent event) {
        LOGGER.info("退款回调通知后异步更新订单[{}]状态。",event.getWeiXinRefundPacket().getOut_trade_no());
        rpgOrderService.updateOrder(event.getWeiXinRefundPacket());
    }

}

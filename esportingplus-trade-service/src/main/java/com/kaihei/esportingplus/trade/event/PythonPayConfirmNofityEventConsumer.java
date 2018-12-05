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
public class PythonPayConfirmNofityEventConsumer extends EventConsumer {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private RPGOrderService rpgOrderService;

    /**
     * 支付确认回调后异步更新订单状态
     * @param event
     */
    @Subscribe
    @AllowConcurrentEvents //开启线程安全
    public void updateOrder(PythonPayConfirmNofityEvent event) {
        LOGGER.info("支付确认回调后异步更新订单[{}]状态。",event.getWeiXinPayConfirmPacket().getOut_trade_no());
        rpgOrderService.updateOrder(event.getWeiXinPayConfirmPacket());
    }

}

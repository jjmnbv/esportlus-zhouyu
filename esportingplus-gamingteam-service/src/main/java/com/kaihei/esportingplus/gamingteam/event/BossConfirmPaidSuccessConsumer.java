package com.kaihei.esportingplus.gamingteam.event;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.kaihei.esportingplus.common.event.EventConsumer;
import com.kaihei.esportingplus.gamingteam.annotation.RedisDistlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 老板确认支付成功事件消费
 *
 * @author liangyi
 */
@Component
public class BossConfirmPaidSuccessConsumer extends EventConsumer {

    @Autowired
    RPGBossConfirmPaidSuccessHandler rpgBossConfirmPaidSuccessHandler;
    @Autowired
    PVPBossConfirmPaidSuccessHandler pvpBossConfirmPaidSuccessHandler;

    /**
     * 老板确认支付成功, 判断是否应该退款
     * @param bossConfirmPaidSuccessEvent
     */
    @Subscribe
    @AllowConcurrentEvents
    @RedisDistlock
    public void handleBossConfirmPaidSuccess(
            BossConfirmPaidSuccessEvent bossConfirmPaidSuccessEvent) {
        String bossUid = bossConfirmPaidSuccessEvent.getBossUid();
        String teamSequence = bossConfirmPaidSuccessEvent.getSequence();
        String orderSequence = bossConfirmPaidSuccessEvent.getOrderSequence();
        if (bossConfirmPaidSuccessEvent.isRPG()) {
            // RPG 车队老板确认支付逻辑
            rpgBossConfirmPaidSuccessHandler.process(bossUid, teamSequence, orderSequence);
        }
        // PVP 车队老板确认支付逻辑
        pvpBossConfirmPaidSuccessHandler.process(bossUid, teamSequence, orderSequence);

    }


}

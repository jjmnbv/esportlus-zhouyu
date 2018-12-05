package com.kaihei.esportingplus.payment.service;

import com.kaihei.esportingplus.payment.api.params.ConsumeGCoinParams;

/**
 * 暴鸡币打赏接口
 *
 * @author tangtao
 */
public interface GCoinRewardService {

    /**
     * 消费暴鸡币兑换暴击值
     */
    void consumeGCoin(String orderId);

    /**
     * 创建订单
     */
    String gCoinCreateOrder(ConsumeGCoinParams consumeGCoinParams);

}

package com.kaihei.esportingplus.gamingteam.event.weixin;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.event.EventConsumer;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.gamingteam.api.params.WxTeamOrderCancelParams;
import com.kaihei.esportingplus.gamingteam.api.params.WxTeamOrderPushData;
import com.kaihei.esportingplus.gamingteam.data.manager.WxSmallProgramPushService;
import com.kaihei.esportingplus.trade.api.feign.RPGOrdersServiceClient;
import com.kaihei.esportingplus.trade.api.vo.OrderVO;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单取消事件消费
 * @author liangyi
 */
@Component
public class WeXinOrderCancelEventConsumer extends EventConsumer {

    @Autowired
    WxSmallProgramPushService wxSmallProgramPushService;

    @Autowired
    RPGOrdersServiceClient RPGOrdersServiceClient;

    @Autowired
    WeXinOrderSupport weXinOrderSupport;

    private static final Logger logger = LoggerFactory.getLogger(WeXinOrderCancelEventConsumer.class);

    /**
     * 向队员推送订单取消服务通知
     * @param weXinOrderCancelEvent
     */
    @Subscribe
    @AllowConcurrentEvents
    public void pushOrderCancelMessage(WeXinOrderCancelEvent weXinOrderCancelEvent){
        WxTeamOrderCancelParams wxTeamOrderCancelParams =
                BeanMapper.map(weXinOrderCancelEvent, WxTeamOrderCancelParams.class);
        String teamSequence = weXinOrderCancelEvent.getTeamSequence();
        List<String> memberUidList = weXinOrderCancelEvent.getMemberUidList();
        if (ObjectTools.isEmpty(memberUidList)) {
            // 如果车队中只有队长, 不发送微信服务通知
            return ;
        }
        List<WxTeamOrderPushData> wxOrderList = new ArrayList<>(20);
        logger.info(">> 微信推送[订单取消]服务通知, 开始调用交易服务的 getByTeamSequenceIdAndUids,"
                + "  车队序列号: {}, 车队队员 uidList: {}", teamSequence, memberUidList);
        ResponsePacket<List<OrderVO>> teamOrderListResp = RPGOrdersServiceClient
                .getByTeamSequenceIdAndUids(teamSequence, memberUidList);
        if (teamOrderListResp.responseSuccess()) {
            List<OrderVO> orderVOList = teamOrderListResp.getData();
            List<OrderVO> bossOrderList = new ArrayList<>();
            weXinOrderSupport.filterWeXinBossOrder(wxOrderList, orderVOList, bossOrderList);
            wxTeamOrderCancelParams.setOrders(wxOrderList);
            logger.info(">> 微信推送[订单取消]服务通知, 车队序列号: {}, 微信订单参数: {}",
                    teamSequence, wxOrderList);
        } else {
            logger.error(">> 微信推送[订单取消]服务通知, 调用交易服务的 getByTeamSequenceIdAndUids 错误!"
                    + "  车队序列号: {}, 错误信息: {}", teamSequence, teamOrderListResp);
        }
        ResponsePacket<Void> orderCancelResp = wxSmallProgramPushService
                .pushOrderCancel(wxTeamOrderCancelParams);
        logger.info(">> 微信推送[订单取消]服务通知, 车队序列号: {}, 调用微信服务结果: {}",
                teamSequence, orderCancelResp);
    }

}

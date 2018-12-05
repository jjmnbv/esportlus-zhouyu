package com.kaihei.esportingplus.user.event;

import com.google.common.eventbus.Subscribe;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.sensors.service.SensorsAnalyticsService;
import com.kaihei.esportingplus.core.api.enums.MessageTypeEnum;
import com.kaihei.esportingplus.core.api.feign.MsgSendServiceClient;
import com.kaihei.esportingplus.core.api.params.MessageSendParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuyang
 * @Description
 * @Date 2018/12/3 16:11
 **/
@Component
public class UserRelationConsumer {

    @Autowired
    private MsgSendServiceClient sendServiceClient;

    @Autowired
    private SensorsAnalyticsService sensorsAnalyticsService;

    public UserRelationConsumer() {
        EventBus.register(this);
    }

    /**
     * 发送im
     *
     * @param event
     */
    @Subscribe
    public void onFollowEvent_im(FollowEvent event) {
        MessageSendParam param = new MessageSendParam();
        param.setSender(event.getUid());
        param.setReciever(event.getFollowId());
        param.setTemplateId(1);
        Map<String, String> data = new HashMap<>();
        data.put("content", event.getUid() + "关注了你");
        param.setData(data);
        param.setMessageType(MessageTypeEnum.CHAT);
        sendServiceClient.send(param);
    }

    /**
     * 发送神策
     *
     * @param event
     */
    @Subscribe
    public void onFollowEvent_sensors(FollowEvent event) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("action_type", "1");
        properties.put("target_id", event.getFollowId());
        sensorsAnalyticsService.track(event.getUid(), "Follow", properties);
    }

    /**
     * 发送神策
     *
     * @param event
     */
    @Subscribe
    public void onUnFollowEvent_sensors(UnFollowEvent event) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("action_type", "2");
        properties.put("target_id", event.getFollowId());
        sensorsAnalyticsService.track(event.getUid(), "Follow", properties);
    }

}

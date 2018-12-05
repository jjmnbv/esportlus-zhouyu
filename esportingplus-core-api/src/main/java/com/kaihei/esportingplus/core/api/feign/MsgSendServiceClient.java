package com.kaihei.esportingplus.core.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.core.api.params.MessageCustomParam;
import com.kaihei.esportingplus.core.api.params.MessageSendParam;
import com.kaihei.esportingplus.core.api.params.PushParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author liuyang
 * @Description 消息发送api
 * @Date 2018/10/26 11:17
 **/
@FeignClient(name = "esportingplus-core-service", path = "/message", fallbackFactory = MsgSendServiceClientFallback.class)
public interface MsgSendServiceClient {

    /**
     * 根据模板发送消息
     * 包含单聊、群聊、系统消息
     * @param messageSendParam
     * @return
     */
    @PostMapping("/send")
    ResponsePacket<Boolean> send(@RequestBody MessageSendParam messageSendParam);

    /**
     * 用户自定义消息发送（不推荐， 推荐使用模板发送）
     * @param messageSendParam
     * @return
     */
//    @PostMapping("/custom/send")
//    ResponsePacket<Boolean> sendCustom(@RequestBody MessageCustomParam messageSendParam);

    /**
     * 发送消息
     * 可以选择标签，标签组合、指定用户、 平台 来发送消息
     * @param pushParam
     * @return
     */
    @PostMapping("/push")
    ResponsePacket<String> push(@RequestBody PushParam pushParam);

}

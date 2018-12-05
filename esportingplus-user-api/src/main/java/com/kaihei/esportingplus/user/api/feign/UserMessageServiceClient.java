package com.kaihei.esportingplus.user.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.user.api.vo.SendFreeTeamMessageVo;
import com.kaihei.esportingplus.user.api.vo.SendMessageVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author zhaozhenlin
 * @description: 发送消息服务client接口
 * @date: 2018/10/10 17:52
 */
@FeignClient(name = "esportingplus-user-service", path = "user/message", fallbackFactory = UserMessageServiceClientFallbackFactory.class)
public interface UserMessageServiceClient {
    /**
     * 发送系统通知消息
     *
     * @param messageVo 发送系统消息参数
     * @return ResponsePacket<Void>
     */
    @PostMapping("/send")
    ResponsePacket<Void> sendMessage(@RequestBody SendMessageVo messageVo);

    @PostMapping("/freeteam/message/send")
    ResponsePacket<Void> SendFreeTeamMessage(@RequestBody SendFreeTeamMessageVo messageVo);
}

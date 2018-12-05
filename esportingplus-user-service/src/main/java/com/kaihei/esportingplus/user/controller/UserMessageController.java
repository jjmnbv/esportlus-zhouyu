package com.kaihei.esportingplus.user.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.user.api.feign.UserMessageServiceClient;
import com.kaihei.esportingplus.user.api.vo.SendFreeTeamMessageVo;
import com.kaihei.esportingplus.user.api.vo.SendMessageVo;
import com.kaihei.esportingplus.user.external.message.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhaozhenlin
 * @description: 发送消息服务
 * @date: 2018/10/10 17:47
 */
@RestController
@RequestMapping("/user/message")
public class UserMessageController implements UserMessageServiceClient {

    @Autowired
    private SendMessageService sendMessageService;

    @Override
    public ResponsePacket<Void> sendMessage(@RequestBody SendMessageVo messageVo) {
        sendMessageService.sendMessage(messageVo);
        return ResponsePacket.onSuccess();
    }

    @Override
    @PostMapping("/freeteam/message/send")
    public ResponsePacket<Void> SendFreeTeamMessage(@RequestBody SendFreeTeamMessageVo messageVo) {
        sendMessageService.SendFreeTeamMessage(messageVo);
        return ResponsePacket.onSuccess();
    }
}

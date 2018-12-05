package com.kaihei.esportingplus.core.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.core.api.feign.MsgSendServiceClient;
import com.kaihei.esportingplus.core.api.params.MessageCustomParam;
import com.kaihei.esportingplus.core.api.params.MessageSendParam;
import com.kaihei.esportingplus.core.api.params.PushParam;
import com.kaihei.esportingplus.core.domain.service.MessageSendService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author liuyang
 * @Description 消息发送
 * @Date 2018/10/29 11:24
 **/
@RestController
@RequestMapping("/message")
@Api(tags = {"消息发送"})
public class MsgSendController implements MsgSendServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(MsgSendController.class);

    @Autowired
    private MessageSendService messageSendService;

    @Override
    public ResponsePacket<Boolean> send(@RequestBody MessageSendParam messageSendParam) {
        logger.debug("cmd=MsgSendController.send | param=" + JacksonUtils.toJson(messageSendParam));
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, messageSendParam);
        return ResponsePacket.onSuccess(messageSendService.send(messageSendParam));
    }

//    @Override
//    public ResponsePacket<Boolean> sendCustom(@RequestBody MessageCustomParam messageSendParam) {
//        logger.debug("cmd=MsgSendController.sendCustom | param=" + JacksonUtils.toJson(messageSendParam));
//        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, messageSendParam);
//        return ResponsePacket.onSuccess(messageSendService.sendCustom(messageSendParam));
//    }

    @Override
    public ResponsePacket<String> push(@RequestBody PushParam pushParam) {
        logger.debug("cmd=MsgSendController.push | param=" + JacksonUtils.toJson(pushParam));
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, pushParam);
        return ResponsePacket.onSuccess(messageSendService.push(pushParam));
    }
}

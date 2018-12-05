package com.kaihei.esportingplus.core.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.core.api.feign.PushMessageServiceClient;
import com.kaihei.esportingplus.core.api.params.PushMessageParam;
import com.kaihei.esportingplus.core.api.vo.PageInfo;
import com.kaihei.esportingplus.core.domain.service.PushMessageService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: esportingplus
 * @description: 消息推送Controller
 * @author: xusisi
 * @create: 2018-12-01 11:47
 **/
@RestController
@Api(tags = {"消息推送水相关API"})
@RequestMapping("/push_message")
public class PushMessageController implements PushMessageServiceClient {

    private static Logger logger = LoggerFactory.getLogger(PushMessageController.class);

    @Autowired
    private PushMessageService pushMessageService;

    @Override
    public ResponsePacket createPushMessage(@RequestBody PushMessageParam pushMessageParam) {
        logger.info("PushMessageParam :{} ", FastJsonUtils.toJson(pushMessageParam));
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, pushMessageParam);
        return ResponsePacket.onSuccess(pushMessageService.createMessagePush(pushMessageParam));

    }

    @Override
    public ResponsePacket<PageInfo> getRecords(@RequestParam(value = "page" ,required = true) Integer page,
                                               @RequestParam(value = "size" ,required = true) Integer size) {
        logger.info("page :{} ,size : {} ", page,size);
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, page,size);
        return ResponsePacket.onSuccess(pushMessageService.getRecords(page,size));
    }

}

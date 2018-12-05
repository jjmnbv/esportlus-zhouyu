package com.kaihei.esportingplus.core.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.core.api.feign.MpPushServiceClient;
import com.kaihei.esportingplus.core.api.params.MPPushParam;
import com.kaihei.esportingplus.core.api.params.MpFormUploadParam;
import com.kaihei.esportingplus.core.domain.service.MpPushService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author liuyang
 * @Description 小程序发送消息
 * @Date 2018/11/5 14:32
 **/
@RestController
@RequestMapping("/message")
@Api(tags = {"小程序发送"})
public class MpPushController implements MpPushServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(MpPushController.class);

    @Autowired
    private MpPushService mpPushService;

    @Override
    public ResponsePacket<Boolean> push(@RequestBody List<MPPushParam> params) {
        logger.debug("cmd=MpPushController.push | param="+ FastJsonUtils.toJson(params));
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, params);
        return ResponsePacket.onSuccess(mpPushService.push(params));
    }

    @Override
    public ResponsePacket<Boolean> upload(@RequestBody MpFormUploadParam param) {
        logger.debug("cmd=MpPushController.upload | param="+ FastJsonUtils.toJson(param));
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, param);
        return ResponsePacket.onSuccess(mpPushService.upload(param));
    }
}

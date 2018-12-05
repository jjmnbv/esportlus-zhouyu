package com.kaihei.esportingplus.core.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.core.api.feign.SMSServiceClient;
import com.kaihei.esportingplus.core.api.params.AuthenticationParam;
import com.kaihei.esportingplus.core.api.params.SmsCredentialParam;
import com.kaihei.esportingplus.core.api.params.SmsSendParam;
import com.kaihei.esportingplus.core.data.dto.SmsSendDto;
import com.kaihei.esportingplus.core.domain.service.SMSSendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author liuyang
 * @Description 短信發送控制類
 * @Date 2018/10/24 16:03
 **/

@RestController
@RequestMapping("/message/sms")
@Api(tags = {"短信服务接口"})
public class SMSSendController implements SMSServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(SMSSendController.class);

    @Autowired
    private SMSSendService smsSendService;

    @ApiOperation(value = "发送短信")
    @Override
    public ResponsePacket<Boolean> smsSend(@RequestBody SmsSendParam param) {
        logger.debug("cmd=SMSSendController.smsSend | param="+ FastJsonUtils.toJson(param));
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, param);
        SmsSendDto dto = new SmsSendDto();
        BeanUtils.copyProperties(param, dto);
       return ResponsePacket.onSuccess(smsSendService.send(dto));
    }

    @Override
    public ResponsePacket<Boolean> authenticationSend(@RequestBody AuthenticationParam param) {
        logger.debug("cmd=SMSSendController.authenticationSend | param="+ FastJsonUtils.toJson(param));
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, param);
        SmsSendDto dto = new SmsSendDto();
        BeanUtils.copyProperties(param, dto);
        dto.setTemplateId(1);
        return ResponsePacket.onSuccess(smsSendService.send(dto));
    }

    @ApiOperation(value = "短信验证码校验")
    @Override
    public ResponsePacket<Boolean> credential(
            @RequestBody SmsCredentialParam param) {
        logger.debug("cmd=SMSSendController.authenticationSend | param="+ FastJsonUtils.toJson(param));
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, param);
        return ResponsePacket.onSuccess(smsSendService.credential(param));
    }
}

package com.kaihei.esportingplus.core.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.core.api.params.AuthenticationParam;
import com.kaihei.esportingplus.core.api.params.SmsCredentialParam;
import com.kaihei.esportingplus.core.api.params.SmsSendParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author liuyang
 * @Description 短信feign
 * @Date 2018/10/24 14:28
 **/
@FeignClient(name = "esportingplus-core-service",path = "/message/sms",fallbackFactory =SMSServiceClientFallback.class)
public interface SMSServiceClient {

    @PostMapping(value = "/send")
    ResponsePacket<Boolean> smsSend(@RequestBody SmsSendParam param);

    @PostMapping(value = "/authentication/send")
    ResponsePacket<Boolean> authenticationSend(@RequestBody AuthenticationParam param);

    @PostMapping(value = "/credential")
    ResponsePacket<Boolean> credential(@RequestBody SmsCredentialParam param);
}

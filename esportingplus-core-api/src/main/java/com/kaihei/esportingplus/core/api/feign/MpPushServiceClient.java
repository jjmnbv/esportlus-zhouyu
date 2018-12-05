package com.kaihei.esportingplus.core.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.core.api.params.MPPushParam;
import com.kaihei.esportingplus.core.api.params.MpFormUploadParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author liuyang
 * @Description //小程序
 * @Date 2018/11/5 14:26
 **/
@FeignClient(name = "esportingplus-core-service", path = "/message", fallbackFactory = MpPushServiceClientFallback.class)
public interface MpPushServiceClient {

    @PostMapping("/miniprogram/push")
    ResponsePacket<Boolean> push(@RequestBody List<MPPushParam> params);

    @PostMapping("miniprogram/upload")
    ResponsePacket<Boolean> upload(@RequestBody MpFormUploadParam params);

}

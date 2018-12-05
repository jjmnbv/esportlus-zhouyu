package com.kaihei.esportingplus.core.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.core.api.params.PushMessageParam;
import com.kaihei.esportingplus.core.api.vo.PageInfo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @program: esportingplus
 * @description: 推送消息
 * @author: xusisi
 * @create: 2018-12-04 15:54
 **/
@FeignClient(name = "esportingplus-core-service", path = "/push_message", fallbackFactory = PushMessageServiceClientFallback.class)
public interface PushMessageServiceClient {

    @PostMapping("/message")
    public ResponsePacket createPushMessage(@RequestBody PushMessageParam pushMessageParam);

    @GetMapping("/list")
    public ResponsePacket<PageInfo> getRecords(@RequestParam(value = "page" ,required = true) Integer page,
                                               @RequestParam(value = "size" ,required = true) Integer size);

}

package com.kaihei.esportingplus.marketing.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.marketing.api.event.UserExemptionEvent;
import com.kaihei.esportingplus.marketing.api.vo.ExemptionTaskVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author zl.zhao
 * @description:新版本用户免单任务接口
 * @date: 2018/10/25 17:43
 */
@FeignClient(name = "esportingplus-marketing-service", path = "/version", fallbackFactory = UserNewVersionExemptionFallbackClient.class)
public interface UserNewVersionExemptionClient {
    /**
     * 新版本用户启动免单任务
     *
     * @param event event
     * @return ResponsePacket<Void>
     */
    @PostMapping("/exemption")
    public ResponsePacket<ExemptionTaskVo> exemptionTask(@RequestBody UserExemptionEvent event);
}

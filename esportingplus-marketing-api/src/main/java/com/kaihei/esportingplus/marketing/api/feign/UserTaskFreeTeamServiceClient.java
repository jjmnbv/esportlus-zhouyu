package com.kaihei.esportingplus.marketing.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.marketing.api.event.TeamFreeEvent;
import com.kaihei.esportingplus.marketing.api.event.TeamFreeObtainstarEvent;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 用户免费车队任务服务
 *
 * @author xiekeqing
 * @date 2018/10/9 11:29
 * @version: 1.0
 */

@FeignClient(name = "esportingplus-marketing-service", path = "/usertask/freeteam")
public interface UserTaskFreeTeamServiceClient {

    /**
     * 免费车队结束车队
     *
     * @param params 用户车队参数
     * @return ResponsePacket<Void>
     */
    @PostMapping("/finish")
    ResponsePacket<Void> finish(@RequestBody TeamFreeEvent params);

    /**
     * 鸡分兑换暴击值
     *
     * @param params 用户车队和评分参数
     * @return ResponsePacket<Void>
     */
    @PostMapping("/obtainstar")
    ResponsePacket<Void> obtainstar(@RequestBody TeamFreeObtainstarEvent params);

}

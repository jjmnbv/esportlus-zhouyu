package com.kaihei.esportingplus.marketing.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.marketing.api.event.CoinConsumeEvent;
import com.kaihei.esportingplus.marketing.api.event.UserRegistEvent;
import com.kaihei.esportingplus.marketing.api.vo.ShareVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Auther: chen.junyong
 * @Date: 2018-10-15 15:00
 * @Description:
 */
@FeignClient(name = "esportingplus-marketing-service", path = "/invit", fallbackFactory = UserInvitShareServiceClientFallbackFactory.class)
public interface UserInvitShareServiceClient {

    /**
     * 用户邀请注册任务接口
     * @param event
     * @return
     */
    @PostMapping("/share/task")
    ResponsePacket shareTask(@RequestBody UserRegistEvent event);

    /**
     * 消费暴击币奖励接口
     * @param event
     * @return
     */
    @PostMapping("/baojicoin/award")
    ResponsePacket awardTask(@RequestBody CoinConsumeEvent event);

    @GetMapping("/get/share")
    public ResponsePacket<ShareVo> sharePoint(@RequestParam(value = "uid", required = false) String uid,
                                              @RequestParam(value = "shareuid" , required = false) String shareuid,
                                              @RequestParam(value = "type" , required = true) String type);
}

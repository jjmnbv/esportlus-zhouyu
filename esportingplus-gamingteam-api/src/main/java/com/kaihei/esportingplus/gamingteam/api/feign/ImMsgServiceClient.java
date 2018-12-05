package com.kaihei.esportingplus.gamingteam.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.gamingteam.api.params.ImDismissGroupParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImEndTeamMsgParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImFullMsgParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupCommonParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupJoinParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupLeavelParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImMatchParams;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "esportingplus-gamingteam-service",
        path = "/immsg", fallbackFactory = ImMsgServiceFallbackFactory.class)
public interface ImMsgServiceClient {
    /**
     * 创建用户组
     * @param params
     */
    @PostMapping("/team/group/create")
    public ResponsePacket<Boolean> createGroup(@RequestBody ImGroupCommonParams params);

    /**
     * 加入用户组，包括发送消息
     * @param params
     */
    @PostMapping("/team/group/join")
    public ResponsePacket<Boolean> joinGroup(@RequestBody ImGroupJoinParams params);

    /**
     * 离开群组，包括发送消息
     * @param params
     */
    @PostMapping("/team/group/leave")
    public ResponsePacket<Void> leaveGroup(@RequestBody ImGroupLeavelParams params);

    /**
     * 发送满员通知消息
     * @param params
     */
    @PostMapping("/team/member/full/send")
    public ResponsePacket<Void> sendFullMembersMsg(@RequestBody ImFullMsgParams params);

    /**
     * 队长结束车队通知
     * @param params
     */
    @PostMapping("/team/end")
    public ResponsePacket<Void> endTeam(@RequestBody ImEndTeamMsgParams params);

    /**
     * 解散群组通知
     * @param params
     */
    @PostMapping("/team/group/dismiss")
    public ResponsePacket<Void> dismissGroup(@RequestBody ImDismissGroupParams params);

    /**
     * 匹配失败消息通知
     * @param params
     * @return
     */
    @PostMapping("/match/send")
    public ResponsePacket<Void> sendMatchMsg(@RequestBody ImMatchParams params);
}

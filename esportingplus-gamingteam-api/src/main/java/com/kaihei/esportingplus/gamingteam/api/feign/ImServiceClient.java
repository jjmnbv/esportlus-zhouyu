package com.kaihei.esportingplus.gamingteam.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.gamingteam.api.params.TeamImCmdMsgBaseParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupBaseParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupCommonParams;
import com.kaihei.esportingplus.gamingteam.api.params.TeamImCmdMsgOutMemberParams;
import com.kaihei.esportingplus.gamingteam.api.params.UserNameImCmdMsgParams;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "esportingplus-gamingteam-service",
        path = "/im", fallbackFactory = ImServiceFallbackFactory.class)
public interface ImServiceClient {
    @PostMapping("/group/create")
    public ResponsePacket<Void> createGroup(@RequestBody ImGroupCommonParams params);

    /**
     * 加入用户组
     * @param params
     * @return
     */
    @PostMapping("/group/join")
    public ResponsePacket<Void> joinGroup(@RequestBody ImGroupCommonParams params);
    /**
     * 退出用户组
     * @param params
     * @return
     */
    @PostMapping("/group/leave")
    public ResponsePacket<Void> leaveGroup(@RequestBody ImGroupBaseParams params);

    /**
     * 解散用户组
     * @param params
     * @return
     */
    @PostMapping("/group/dismiss")
    public ResponsePacket<Void> dismissGroup(@RequestBody ImGroupBaseParams params);

    /**
     * 队长开车
     * @param params
     * @return
     */
    @PostMapping("/team/start")
    public  ResponsePacket<Void> startTeam(@RequestBody TeamImCmdMsgBaseParams params);


    /**
     * 踢出车队
     * @param params
     * @return
     */
    @PostMapping("/team/member/out")
    public ResponsePacket<Void> outTeam(@RequestBody TeamImCmdMsgOutMemberParams params);

    /**
     * 解散车队
     * @param params
     * @return
     */
    @PostMapping("/team/dismiss")
    public ResponsePacket<Void> dismissTeam(@RequestBody TeamImCmdMsgBaseParams params);

    /**
     * 车队位置改变
     * @param params
     * @return
     */
    @PostMapping("/team/count/change")
    public ResponsePacket<Void> changeTeamCount(@RequestBody TeamImCmdMsgBaseParams params);

    /**
     *  车队服务结束通知
     * @param params
     * @return
     */
    @PostMapping("/team/server/end")
    public ResponsePacket<Void> endTeamServer(@RequestBody TeamImCmdMsgBaseParams params);

    /**
     * 支付完成通知
     * @param params
     * @return
     */
    @PostMapping("/team/member/pay")
    public ResponsePacket<Void> paidFinish(@RequestBody TeamImCmdMsgBaseParams params);

    /**
     * 确认入团通知
     * @param params
     * @return
     */
    @PostMapping("/team/member/confirm")
    public ResponsePacket<Void> confirmInTeam(@RequestBody TeamImCmdMsgBaseParams params);

    /**
     * 加入车队通知
     * @param params
     * @return
     */
    @PostMapping("/team/member/join")
    public ResponsePacket<Void> joinTeam(@RequestBody UserNameImCmdMsgParams params);
    /**
     * 退出车队通知
     * @param params
     * @return
     */
    @PostMapping("/team/member/exit")
    public  ResponsePacket<Void> exitTeam(@RequestBody UserNameImCmdMsgParams params);
}

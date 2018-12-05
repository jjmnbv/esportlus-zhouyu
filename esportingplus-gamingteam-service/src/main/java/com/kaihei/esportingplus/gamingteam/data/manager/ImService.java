package com.kaihei.esportingplus.gamingteam.data.manager;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.gamingteam.api.params.TeamImCmdMsgBaseParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupBaseParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupCommonParams;
import com.kaihei.esportingplus.gamingteam.api.params.TeamImCmdMsgOutMemberParams;
import com.kaihei.esportingplus.gamingteam.api.params.UserNameImCmdMsgParams;

public interface ImService {

    /**
     * 创建用户组
     * @param params
     * @return
     */
    public ResponsePacket<Void> createGroup(ImGroupCommonParams params);

    /**
     * 加入用户组
     * @param params
     * @return
     */
    public ResponsePacket<Void> joinGroup(ImGroupCommonParams params);
    /**
     * 退出用户组
     * @param params
     * @return
     */
    public ResponsePacket<Void> leaveGroup(ImGroupBaseParams params);

    /**
     * 解散用户组
     * @param params
     * @return
     */
    public ResponsePacket<Void> dismissGroup(ImGroupBaseParams params);

    /**
     * 队长开车
     * @param params
     * @return
     */
    public  ResponsePacket<Void> startTeam(TeamImCmdMsgBaseParams params);

    /**
     * 踢出车队  踢出车队先发给被T人踢出车队通知，再发送退出车队通知给其它人
     * @param params
     * @return
     */
    public ResponsePacket<Void> outTeam(TeamImCmdMsgOutMemberParams params);

    /**
     * 解散车队 发送通知给除队长外的其它人
     * @param params
     * @return
     */
    public ResponsePacket<Void> dismissTeam(TeamImCmdMsgBaseParams params);

    /**
     * 车队位置改变 发送全员，可不传toUsers
     * @param params
     * @return
     */
    public ResponsePacket<Void> changeTeamCount(TeamImCmdMsgBaseParams params);

    /**
     *  车队服务结束通知
     * @param params
     * @return
     */
    public ResponsePacket<Void> endTeamServer(TeamImCmdMsgBaseParams params);

    /**
     * 支付完成通知 发送给全员，可不传toUsers
     * @param params
     * @return
     */
    public ResponsePacket<Void> paidFinish(TeamImCmdMsgBaseParams params);

    /**
     * 确认入团通知 发送给全员，可不传toUsers
     * @param params
     * @return
     */
    public ResponsePacket<Void> confirmInTeam(TeamImCmdMsgBaseParams params);

    /**
     * 加入车队通知 发送给除自己外的其它人
     * @param params
     * @return
     */
    public ResponsePacket<Void> joinTeam(UserNameImCmdMsgParams params);
    /**
     * 退出车队通知 发送给除自己外的其它人
     * @param params
     * @return
     */
    public  ResponsePacket<Void> exitTeam(UserNameImCmdMsgParams params);
}

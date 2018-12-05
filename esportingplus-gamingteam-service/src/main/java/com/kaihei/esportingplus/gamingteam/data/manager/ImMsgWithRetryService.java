package com.kaihei.esportingplus.gamingteam.data.manager;

import com.kaihei.esportingplus.gamingteam.api.params.ImDismissGroupParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImEndTeamMsgParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImFullMsgParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupCommonParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupJoinParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupLeavelParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImMatchParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImTeamStartParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImTeamStatusChangeParams;

public interface ImMsgWithRetryService {

    /**
     * 创建用户组
     * @param params
     */
    public void createGroup(ImGroupCommonParams params);

    /**
     * 加入用户组，包括发送消息
     * @param params
     */
    public void joinGroup(ImGroupJoinParams params);

    /**
     * 离开群组，包括发送消息
     * @param params
     */
    public void leaveGroup(ImGroupLeavelParams params);

    /**
     * 发送满员通知消息
     * @param params
     */
    public void sendFullMembersMsg(ImFullMsgParams params);

    /**
     * 队长结束车队通知
     * @param params
     */
    public void endTeam(ImEndTeamMsgParams params);

    /**
     * 解散群组通知
     * @param params
     */
    public void dismissGroup(ImDismissGroupParams params);

    /**
     * 匹配消息通知,成功或者失败
     * @param params
     */
    public void  sendMatchMsg(ImMatchParams params);

    /**
     * 车队状态变化接口，如成员的准备取消准备
     * @param params
     */
    public void sendTeamStatusChange(ImTeamStatusChangeParams params);

    /**
     * 车队开车通知
     * @param params
     */
    public void sendTeamStart(ImTeamStartParams params);
}

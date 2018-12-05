package com.kaihei.esportingplus.user.external.message;

import com.kaihei.esportingplus.user.api.vo.SendFreeTeamMessageVo;
import com.kaihei.esportingplus.user.api.vo.SendMessageVo;

/**
 * @author zhaozhenlin
 * @description: 发送消息
 * @date: 2018/10/9 11:32
 */
public interface SendMessageService {
    /**
     * 发送消息
     * @param messageVo
     */
    public void sendMessage(SendMessageVo messageVo);

    /**
     * 发送消息(分享拉新)
     * @param messageVo
     */
    public void SendFreeTeamMessage(SendFreeTeamMessageVo messageVo);
}

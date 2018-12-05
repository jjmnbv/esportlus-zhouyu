package com.kaihei.esportingplus.core.message;

import com.kaihei.esportingplus.core.api.params.PushMessageParam;

/**
 * @program: esportingplus
 * @description: 推送消息
 * @author: xusisi
 * @create: 2018-12-01 12:11
 **/
public interface PushMessagePublish {

    public Boolean sendMessage(PushMessageParam pushMessageParam);


    public Boolean sendSystemMessage(PushMessageParam pushMessageParam);
}

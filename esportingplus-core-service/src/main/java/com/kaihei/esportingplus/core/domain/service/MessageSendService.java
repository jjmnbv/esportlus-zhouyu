package com.kaihei.esportingplus.core.domain.service;

import com.kaihei.esportingplus.core.api.params.MessageCustomParam;
import com.kaihei.esportingplus.core.api.params.MessageSendParam;
import com.kaihei.esportingplus.core.api.params.PushParam;

/**
 * @Author liuyang
 * @Description
 * @Date 2018/10/29 11:59
 **/
public interface MessageSendService {

    boolean send(MessageSendParam param);

    boolean sendCustom(MessageCustomParam messageSendParam);

    String push(PushParam pushParam);
}

package com.kaihei.esportingplus.core.domain.service;

import com.kaihei.esportingplus.core.api.params.PushMessageParam;
import com.kaihei.esportingplus.core.api.vo.PageInfo;
import com.kaihei.esportingplus.core.domain.entity.PushMessageRecord;

/**
 * @program: esportingplus
 * @description: 消息推送service
 * @author: xusisi
 * @create: 2018-12-01 12:09
 **/
public interface PushMessageService {

    /***
     * 创建消息推送
     * @param pushMessageParam
     */
    public Boolean createMessagePush(PushMessageParam pushMessageParam);

    public PageInfo<PushMessageRecord> getRecords(Integer page, Integer size);
}

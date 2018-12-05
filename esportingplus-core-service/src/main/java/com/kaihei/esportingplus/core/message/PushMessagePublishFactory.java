package com.kaihei.esportingplus.core.message;

import com.kaihei.esportingplus.core.constant.MessageConstants;

/**
 * @program: esportingplus
 * @description: 推送消息转换器
 * @author: xusisi
 * @create: 2018-12-01 18:46
 **/
public class PushMessagePublishFactory {

    public PushMessagePublish createMessagePublish(Integer type) {
        switch (type) {
            case MessageConstants.Type.RON_YUN_PUSH:
                RonYunPushMessagePublish ronYunPushMessagePublish = new RonYunPushMessagePublish();
                return ronYunPushMessagePublish;
            default:
                break;
        }

        return null;
    }
}

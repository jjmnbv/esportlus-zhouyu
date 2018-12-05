package com.kaihei.esportingplus.core.message;

import com.kaihei.esportingplus.core.constant.MessageConstants;

/**
 * @Author liuyang
 * @Description 创建消息发送
 * @Date 2018/11/2 11:38
 **/
public class MessagePublishFactory {

    public MessagePublish createMessagePublish(Integer type) {
        switch (type) {
            case MessageConstants.Type.PRIVATE:
                MessagePrivatePublish messagePrivatePublish = new MessagePrivatePublish();
                return messagePrivatePublish;
            case MessageConstants.Type.GROUP:
                MessageGroupPublish messageGroupPublish = new MessageGroupPublish();
                return messageGroupPublish;
            case MessageConstants.Type.SYSTEM:
                MessageSystemPublish messageSystemPublish = new MessageSystemPublish();
                return messageSystemPublish;
            case MessageConstants.Type.PUSH:
                    MessageSystemPublish ronyunPush = new MessageSystemPublish();
                    return ronyunPush;
        }

        return null;
    }

}

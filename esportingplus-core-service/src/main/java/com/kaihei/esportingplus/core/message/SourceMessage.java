package com.kaihei.esportingplus.core.message;

import com.kaihei.esportingplus.core.api.params.MessageParam;
import com.kaihei.esportingplus.core.api.params.MessageSendParam;
import com.kaihei.esportingplus.core.domain.entity.MessageTemplate;

/**
 * @Author liuyang
 * @Description
 * @Date 2018/11/17 19:00
 **/
public class SourceMessage {

    private MessageParam param;

    private MessageTemplate template;

    public SourceMessage(MessageParam param, MessageTemplate template) {
        this.param = param;
        this.template = template;
    }

    public SourceMessage() {
    }

    public MessageParam getParam() {
        return param;
    }

    public void setParam(MessageParam param) {
        this.param = param;
    }

    public MessageTemplate getTemplate() {
        return template;
    }

    public void setTemplate(MessageTemplate template) {
        this.template = template;
    }
}

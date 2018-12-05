package com.kaihei.esportingplus.riskrating.event;

import com.kaihei.esportingplus.common.event.Event;
import com.kaihei.esportingplus.riskrating.bean.AlermContent;

public class AlermMsgSendEvent implements Event {
    private String msgtype="text";
    private AlermContent text;

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public AlermContent getText() {
        return text;
    }

    public void setText(AlermContent text) {
        this.text = text;
    }
}

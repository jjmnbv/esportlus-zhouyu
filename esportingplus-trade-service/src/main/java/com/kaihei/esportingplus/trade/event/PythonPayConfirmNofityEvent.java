package com.kaihei.esportingplus.trade.event;

import com.kaihei.esportingplus.common.event.Event;
import com.kaihei.esportingplus.trade.api.params.pay.WeiXinPayConfirmPacket;

public class PythonPayConfirmNofityEvent implements Event {

    private WeiXinPayConfirmPacket weiXinPayConfirmPacket;

    public PythonPayConfirmNofityEvent(
            WeiXinPayConfirmPacket weiXinPayConfirmPacket) {
        this.weiXinPayConfirmPacket = weiXinPayConfirmPacket;
    }

    public WeiXinPayConfirmPacket getWeiXinPayConfirmPacket() {
        return weiXinPayConfirmPacket;
    }

    public void setWeiXinPayConfirmPacket(
            WeiXinPayConfirmPacket weiXinPayConfirmPacket) {
        this.weiXinPayConfirmPacket = weiXinPayConfirmPacket;
    }
}

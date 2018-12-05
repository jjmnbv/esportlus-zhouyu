package com.kaihei.esportingplus.trade.event;

import com.kaihei.esportingplus.common.event.Event;
import com.kaihei.esportingplus.trade.api.params.pay.WeiXinRefundPacket;

public class PythonRefundNofityEvent implements Event {

    private WeiXinRefundPacket weiXinRefundPacket;

    public PythonRefundNofityEvent(
            WeiXinRefundPacket weiXinRefundPacket) {
        this.weiXinRefundPacket = weiXinRefundPacket;
    }

    public WeiXinRefundPacket getWeiXinRefundPacket() {
        return weiXinRefundPacket;
    }

    public void setWeiXinRefundPacket(
            WeiXinRefundPacket weiXinRefundPacket) {
        this.weiXinRefundPacket = weiXinRefundPacket;
    }
}

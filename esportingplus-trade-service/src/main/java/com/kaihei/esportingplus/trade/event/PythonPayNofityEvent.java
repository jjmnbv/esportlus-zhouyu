package com.kaihei.esportingplus.trade.event;

import com.kaihei.esportingplus.common.event.Event;
import com.kaihei.esportingplus.trade.domain.entity.Order;

public class PythonPayNofityEvent implements Event {

    private Order order;
    private String weiXinPayReturnCode;

    public PythonPayNofityEvent(Order order, String weiXinPayReturnCode) {
        this.order = order;
        this.weiXinPayReturnCode = weiXinPayReturnCode;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getWeiXinPayReturnCode() {
        return weiXinPayReturnCode;
    }

    public void setWeiXinPayReturnCode(String weiXinPayReturnCode) {
        this.weiXinPayReturnCode = weiXinPayReturnCode;
    }

}

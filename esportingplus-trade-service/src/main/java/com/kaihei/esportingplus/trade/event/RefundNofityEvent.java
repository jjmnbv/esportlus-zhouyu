package com.kaihei.esportingplus.trade.event;

import com.kaihei.esportingplus.common.event.Event;
import com.kaihei.esportingplus.trade.domain.entity.Order;

public class RefundNofityEvent implements Event {

    private Order order;

    public RefundNofityEvent(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}

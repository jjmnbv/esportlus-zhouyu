package com.kaihei.esportingplus.trade.event;

import com.kaihei.esportingplus.common.event.Event;
import com.kaihei.esportingplus.payment.api.params.OrderIncomeParams;

public class SendProfitEvent implements Event {

    private OrderIncomeParams orderIncomeParams;

    public SendProfitEvent(
            OrderIncomeParams orderIncomeParams) {
        this.orderIncomeParams = orderIncomeParams;
    }

    public OrderIncomeParams getOrderIncomeParams() {
        return orderIncomeParams;
    }

    public void setOrderIncomeParams(
            OrderIncomeParams orderIncomeParams) {
        this.orderIncomeParams = orderIncomeParams;
    }
}

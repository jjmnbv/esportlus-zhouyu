package com.kaihei.esportingplus.trade.event;

import com.kaihei.esportingplus.common.event.Event;
import com.kaihei.esportingplus.trade.domain.entity.Order;
import com.kaihei.esportingplus.trade.domain.entity.OrderItemTeamRPG;
import java.util.List;

/**
 * 創建老闆訂單的消息通知參數
 */

public class CreateOrderEvent implements Event {
    private Order order;
    private OrderItemTeamRPG orderItemTeamRPG;
    private List<Long> couponIds;

    public CreateOrderEvent(){}

    public CreateOrderEvent(Order order, OrderItemTeamRPG orderItemTeamRPG, List<Long> couponIds){
        this.order = order;
        this.orderItemTeamRPG = orderItemTeamRPG;
        this.couponIds = couponIds;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderItemTeamRPG getOrderItemTeamRPG() {
        return orderItemTeamRPG;
    }

    public void setOrderItemTeamRPG(OrderItemTeamRPG orderItemTeamRPG) {
        this.orderItemTeamRPG = orderItemTeamRPG;
    }

    public List<Long> getCouponIds() {
        return couponIds;
    }

    public void setCouponIds(List<Long> couponIds) {
        this.couponIds = couponIds;
    }
}

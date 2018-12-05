package com.kaihei.esportingplus.trade.event;

import org.springframework.stereotype.Component;

@Component
public class CreateOrderEventConsumer /*extends EventConsumer*/ {

/*    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemTeamRepository orderItemTeamRepository;
    @Autowired
    private OrderCouponRepository orderCouponRepository;*/

    /**
     * 创建老板订单
     * @param event
     */
    /*   *//*@Subscribe
    @AllowConcurrentEvents*//*
    @Transactional(rollbackFor = Exception.class)
    public void createOrder(CreateOrderEvent event) {
        //获取实体
        Order order = event.getOrder();
        if (Objects.isNull(order.getDiscountAmount())) {
            order.setDiscountAmount(0);
        }
        OrderItemTeam orderItemTeam = event.getOrderItemTeam();
        List<Long> couponIds = event.getCouponIds();

        //Order实体入库
        orderRepository.insertOrder(order);
        orderItemTeam.setOrderId(order.getId());
        orderItemTeam.setUid(order.getUid());

        //OrderItemTeam实体入库
        orderItemTeamRepository.insertOrderItemTeam(orderItemTeam);

        //OrderCoupon批量入库
        if(couponIds!=null && !couponIds.isEmpty()){
            List<OrderCoupon> orderConponList = new LinkedList<OrderCoupon>();
            OrderCoupon orderCoupon = null;
            for(Long couponId : couponIds){
                orderCoupon = new OrderCoupon();
                orderCoupon.setOrderId(order.getId());
                orderCoupon.setCouponId(couponId);
                orderConponList.add(orderCoupon);
            }
            orderCouponRepository.insertOrderCoupon(orderConponList);
        }*/

//    }
}

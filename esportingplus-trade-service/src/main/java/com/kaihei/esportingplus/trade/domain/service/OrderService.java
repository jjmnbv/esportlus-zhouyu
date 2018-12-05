package com.kaihei.esportingplus.trade.domain.service;


import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.trade.api.params.CheckTeamMemberPayedParams;
import com.kaihei.esportingplus.trade.common.UpdateOrderParams;
import com.kaihei.esportingplus.trade.domain.entity.Order;
import com.kaihei.esportingplus.trade.enums.GameOrderType;
import java.util.List;

public interface OrderService {

    /**
     *@Description: 根据sequence查询订单
     *@param: [id]
     *@return: com.kaihei.esportingplus.trade.domain.entity.Order
     *@throws:
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/11/7 11:50
    */
    Order getBySequenceId(String sequence);
    Order getPvpOrderBySequenceId(String sequence);
    /**
     *@Description: 更新订单entity
     *@param: [orderParm]
     *@return: void
     *@throws:
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/11/7 11:50
    */
    void updateOrder(Order orderParm);

    /**
     *@Description: 退款回调更新订单
     *@param: [refundOrder] 微信退款回调参数
     *@throws:
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/8/8 15:45
     */
    BizExceptionEnum updateRefundOrder(Order refundOrder);

    /**
     *@Description: 更新订单状态
     *@param: [order, 订单
     * memberStatus, 队员状态
     * neddRefund, 是否需要退款
     * profitAmout，暴鸡收益金额]
     *@return: com.kaihei.esportingplus.trade.domain.entity.Order
     *@throws:
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/11/12 17:14
    */
    Order updateOrder(UpdateOrderParams params);

    /**
     *@Description: 校验队员(老板)是否已支付
     *@param: [checkTeamMemberPayedParams]
     *@return: BizExceptionEnum
     *@throws:
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/8/8 20:51
     */
    BizExceptionEnum checkTeamMemberPayed(CheckTeamMemberPayedParams checkTeamMemberPayedParams);

    /**
     *@Description: 校验队员(老板)是否已支付
     *@param: [orderSequence]
     *@return: BizExceptionEnum
     *@throws:
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/8/8 20:51
     */
    BizExceptionEnum checkTeamMemberPayed(String orderSequence);

    /**
     * 订单退款：支付成功后发现不在车队中（非正常支付流程：微信支付停留在密码窗口的毒瘤）
     * @param sequence 订单序列号
     */
    void refundOrder(String sequence, GameOrderType gameOrderType);

    /**
     *@Description: 根据teamSequence、uid列表和是否需要延迟查询订单列表
     *@param: [id, uids, needDelay]
     *@return: java.util.List<com.kaihei.esportingplus.trade.domain.entity.Order>
     *@throws:
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/11/7 11:48
    */
    List<Order> getByTeamSequenceIdAndUids(String teamSequence, List<String> uids,
            boolean needDelay);


    /**
     * 新增订单和优惠券
     * @param order
     * @param couponIds
     */
    void insertOrderAndCoupon(Order order, List<Long> couponIds);

    /**
     * 新增订单
     * @param order
     */
    void insertOrder(Order order);
}

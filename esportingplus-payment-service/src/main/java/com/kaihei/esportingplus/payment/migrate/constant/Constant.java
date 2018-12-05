package com.kaihei.esportingplus.payment.migrate.constant;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

/**
 * @author: tangtao
 **/
public class Constant {


    public static final Sort DEFAULT_SORT = new Sort(new Order(Direction.ASC, "createTime"));
    public static final int DEFAULT_PAGE_SIZE = 5000;

    // 支付方式
    public static final int PAY_TYPE_WECHAT = 2;
    public static final int PAY_TYPE_ALI = 4;
    public static final int PAY_TYPE_QQ = 7;
    public static final int PAY_TYPE_YUN = 666;


    //支付单状态-预支付
    public static final int PAY_ORDER_STATUS_PREPAY = 0;
    //支付单状态-已付款
    public static final int PAY_ORDER_STATUS_PAYED = 1;
    //支付单状态-支付失败
    public static final int PAY_ORDER_STATUS_FAIL = 2;
    //支付单状态-已发货
    public static final int PAY_ORDER_STATUS_DELIVERED = 3;
    //支付单状态-已确认
    public static final int PAY_ORDER_STATUS_FINISH = 4;


    //充值单状态-待付款
    public static final int RECHARGE_STATUS_WAIT = 1;
    //充值单状态-已支付
    public static final int RECHARGE_STATUS_PAYED = 2;


    // 提现类型
    public static final int TRANSFER_TYPE_WX = 1;
    public static final int TRANSFER_TYPE_YUN_ALI = 2;
    public static final int TRANSFER_TYPE_YUN_WX = 3;

    /**
     * 退款方式 1：暴鸡钱包
     */
    public static final int REFUND_WAY_WALLET = 1;
    public static final int REFUND_WAY_WX = 2;
    public static final int REFUND_WAY_ALI = 4;
    public static final int REFUND_WAY_QQ = 7;

    /**
     * 业务订单类型 车队单
     */
    public static final int BILL_TYPE_RECHARGE_WECHAT = 4;
    public static final int BILL_TYPE_RECHARGE_ALI = 13;
    public static final int BILL_TYPE_RECHARGE_QQ = 55;

}

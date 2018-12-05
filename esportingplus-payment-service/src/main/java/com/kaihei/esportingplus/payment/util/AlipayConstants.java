package com.kaihei.esportingplus.payment.util;

/**
 * @ClassName AlipayConstants
 * @Description TODO
 * @Author xusisi
 * @Date 2018/11/22 下午5:28
 */
public class AlipayConstants {

    // 公共错误码：
    /***
     * 交易不存在
     * 检查请求中的交易号和商户订单号是否正确，确认后重新发起
     */
    public static final String ACQ_TRADE_NOT_EXIST = "ACQ.TRADE_NOT_EXIST";

    /**
     * 系统错误
     * 请使用相同的参数再次调用
     */
    public static final String  ACQ_SYSTEM_ERROR = "aop.ACQ.SYSTEM_ERROR";

    /***
     * 交易已完结
     * 该交易已完结，不允许进行退款，确认请求的退款的交易信息是否正确
     */
    public static final String ACQ_TRADE_HAS_FINISHED = "ACQ.TRADE_HAS_FINISHED";

    //撤销订单时返回
    // 交易关闭，无退款
    public static final String RESP_ACTION_CLOSE = "close";

    // 产生了退款
    public static final String RESP_ACTION_REFUND = "refund";

    /***
     * WAIT_BUYER_PAY 交易创建，等待买家付款
     */
    public static final String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";

    /***
     * TRADE_CLOSED 未付款交易超时关闭，或支付完成后全额退款
     */
    public static final String TRADE_CLOSED = "TRADE_CLOSED";

    /***
     * TRADE_SUCCESS 交易支付成功
     */
    public static final String TRADE_SUCCESS = "TRADE_SUCCESS";

    /***
     * TRADE_FINISHED 交易结束，不可退款
     */
    public static final String TRADE_FINISHED = "TRADE_FINISHED";

    //返回参数
    /***
     * 商户订单号
     */
    public static final String RETURN_PARAM_OUT_TRADE_NO = "out_trade_no";

    /**
     * 订单金额
     */
    public static final String RETURN_PARAM_TOTAL_AMOUNT = "total_amount";

    /***
     * 总退款金额
     */
    public static final String RETURN_PARAM_REFUND_FEE = "refund_fee";

    /***
     * 支付宝交易号
     */
    public static final String RETURN_PARAM_TRADE_NO = "trade_no";


    /**
     * 交易状态
     */
    public static final String RETURN_PARAM_TRADE_STATUS = "trade_status";

    /***
     * 交易付款时间
     */
    public static final String RETURN_PARAM_GMT_PAYMENT = "gmt_payment";

}

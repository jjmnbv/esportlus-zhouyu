package com.kaihei.esportingplus.payment.api.enums;

public enum RedisKeyType {
    //redisKey类型

    /***
     * 充值
     */
    RECHARGE("recharge", "充值"),

    /***
     * 支付
     */
    PAYMENT1("payment", "支付"),
    /***
     * 退款
     */
    REFUND("refund", "退款"),

    /**
     * thirdTrade
     */
    THIRDTRADE("thirdTrade", "第三方交易"),





    ///***
    // * 充值
    // */
    //PAYMENT("payment", "充值"),
    /***
     * 提现
     */
    WITHDRAW("withdraw", "提现"),

    /***
     * 提现
     */
    EXCHANGE("exchange", "兑换"),

    /***
     * 暴鸡币支付
     */
    GCOINPAYMENT("gcoinPayment", "暴鸡币支付"),

    /***
     * 暴鸡币退款
     */
    GCOINREFUND("gcoinRefund", "暴鸡币退款");


    private String code;
    private String msg;

    RedisKeyType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }


    public String getCode() {
        return code;
    }


}

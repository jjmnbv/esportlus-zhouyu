package com.kaihei.esportingplus.payment.api.enums;

/**
 * 结算订单类型
 *
 * @author xiaolijun
 **/
public enum OrderTypeEnum {

    /**
     * 退款订单
     */
    REFUND_ORDER("001", "退款订单"),
    /**
     * 提现订单
     */
    WITHDRAW_ORDER("002","提现订单"),
    /**
     * 暴击值兑换暴鸡币订单
     */
    STAR_EXCHANGE_ORDER("003","暴击值兑换暴鸡币订单"),
    /**
     * 暴鸡币打赏订单
     */
    GCOIN_REWARD_ORDER("004","暴鸡币打赏订单"),
    /**
     * 结算订单
     */
    SETTLEMENT_ORDER("005","结算订单"),
    /**
     * 充值暴鸡币订单
     */
    RECHARGE_ORDER("006","充值暴鸡币订单"),
    /**
     * 暴鸡币打赏订单
     */
    GCOIN_PAYMENT_ORDER("007","暴鸡币支付订单"),
    /**
     * 鸡分兑换暴击值订单
     */
    SCORE_EXCHANGE_ORDER("008","鸡分兑换暴击值订单"),
    /**
     * 暴鸡币扣款订单
     */
    GCOIN_WITHDRAW_ORDER("009","暴鸡币扣款订单"),
    /**
     * 暴击值扣款订单
     */
    STAR_WITHDRAW_ORDER("010","暴击值扣款订单");

    private String code;
    private String msg;

    OrderTypeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

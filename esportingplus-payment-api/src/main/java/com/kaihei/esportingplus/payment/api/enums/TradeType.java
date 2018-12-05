package com.kaihei.esportingplus.payment.api.enums;

public enum TradeType {

    RECHARGE("001", "充值"),
    PAYMENT("002", "支付"),
    WITHDRAW("003", "提现"),
    REFUND("004", "退款"),
    ;

    private String code;

    private String msg;

    TradeType(String code, String msg) {
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

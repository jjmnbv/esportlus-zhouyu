package com.kaihei.esportingplus.payment.api.enums;

/**
 * @program: esportingplus-payment
 * @description: 暴鸡币流水类型（001表示打赏，002表示充值）；暴鸡币操作类型（INCREASE表示增加，DECREASE表示减少）
 * @author: xusisi
 * @create: 2018-08-17 11:29
 **/
public enum GCoinBillType {
    /***
     * 001打赏
     */
    COMSUME("001", "打赏"),
    /***
     * 002充值
     */
    PAYMENT("002", "充值"),
    /***
     * 002兑现
     */
    WITHDRAW("002", "兑现"),
    
    /***
     * 金额增加操作
     */
    INCREASE("INCREASE", "增加"),
    /***
     * 金额减少操作
     */
    DECREASE("DECREASE", "减少");

    private String code;

    private String msg;

    GCoinBillType(String code, String msg) {
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

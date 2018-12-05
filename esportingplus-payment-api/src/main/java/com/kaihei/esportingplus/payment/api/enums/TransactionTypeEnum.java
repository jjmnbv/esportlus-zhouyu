package com.kaihei.esportingplus.payment.api.enums;

/**
 * 交易类型
 *
 * @author: xiaolijun
 * @create: 2018-08-17 11:29
 **/
public enum TransactionTypeEnum {
    /***
     * 001 收入
     */
    INCOME("T001", "收入"),
	
    /***
     * 002 支出
     */
    EXPENDITURE("T002", "支出"),
    /***
     * 003 扣款
     */
    WITHDRAWING("T003", "扣款"),
    /***
     * 004 退款
     */
    REFUND("T004", "退款");

    private String code;

    private String msg;

    TransactionTypeEnum(String code, String msg) {
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

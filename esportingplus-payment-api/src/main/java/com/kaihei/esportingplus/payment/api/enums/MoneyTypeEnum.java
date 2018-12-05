package com.kaihei.esportingplus.payment.api.enums;

/**
 * @description: 流水类型枚举
 * @author: xiaolijun
 **/
public enum MoneyTypeEnum {
	//交易状态
    /***
     * 001表示暴鸡币
     */
	GCOIN_MONEY("001", "暴鸡币"),
    /***
     * 002表示暴击值
     */
	STARLIGHT_MONEY("002", "暴击值");

    private String code;

    private String msg;

    MoneyTypeEnum(String code, String msg) {
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

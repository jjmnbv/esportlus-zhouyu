package com.kaihei.esportingplus.payment.api.enums;

/***
 * 扣款操作货币类型
 */
public enum DeductCurrencyTypeEnum {

    /**
     * 暴鸡币
     */
    GCOIN("001", "暴鸡币"),

    /**
     * 暴击值
     */
    STARLIGHT("002", "暴击值");

    private String code;
    private String msg;

    DeductCurrencyTypeEnum(String code, String msg) {
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

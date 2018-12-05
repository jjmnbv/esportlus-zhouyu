package com.kaihei.esportingplus.marketing.api.enums;

/**
 * @author zl.zhao
 * @description:免费券ES存储类型
 * @date: 2018/11/20 15:46
 */
public enum FreeCouponsESTypeEnum {

    /**
     * 注册
     */
    USED("1", "已使用"),


    /**
     * 注册
     */
    EXPIRED("2", "已过期");

    private String code;
    private String msg;

    FreeCouponsESTypeEnum(String code, String msg) {
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

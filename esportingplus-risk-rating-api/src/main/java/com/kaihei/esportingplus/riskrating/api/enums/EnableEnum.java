package com.kaihei.esportingplus.riskrating.api.enums;

/**
 * @author 谢思勇
 */
public enum EnableEnum {
    /**
     * IOS
     */
    DISABLE(0, "禁用"),
    /**
     * PC
     */
    ENABLE(1, "启用");

    /**
     * 编码
     */
    private Integer code;
    /**
     * 说明
     */
    private String desc;


    EnableEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

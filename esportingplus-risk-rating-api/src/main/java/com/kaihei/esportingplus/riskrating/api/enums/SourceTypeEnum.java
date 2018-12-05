package com.kaihei.esportingplus.riskrating.api.enums;

/**
 * @author 谢思勇
 */
public enum SourceTypeEnum {
    /**
     * IOS
     */
    IOS("IOS", "IOS"),
    /**
     * PC
     */
    PC("PC", "PC"),
    /**
     * ANDROID
     */
    ANDROID("ANDROID", "ANDROID");

    /**
     * 编码
     */
    private String code;
    /**
     * 说明
     */
    private String desc;


    SourceTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

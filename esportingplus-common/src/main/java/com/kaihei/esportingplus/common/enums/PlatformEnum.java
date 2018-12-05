package com.kaihei.esportingplus.common.enums;

/**
 * <pre>
 *   客户端平台类型枚举
 *      客户端会在请求后加一个参数 x
 *      x=i 表示请求来自 iOS
 *      x=a 表示请求来自 Android
 *      x=mp 表示请求来自微信小程序
 * </pre>
 *
 * @author liangyi
 */
public enum PlatformEnum {

    /** iOS */
    iOS("iOS", "i", "iOS"),

    /** Android */
    Android("Android", "a", "Android"),

    /** 小程序 */
    MINI_PROGRAM("miniprogram", "mp", "微信小程序"),

    OTHER("other", "", "其他平台类型");



    private String code;
    private String param;
    private String desc;

    PlatformEnum(String code, String param, String desc) {
        this.code = code;
        this.param = param;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getParam() {
        return param;
    }

    public String getDesc() {
        return desc;
    }

    public static PlatformEnum getByCode(String code) {
        PlatformEnum[] values = PlatformEnum.values();
        for (PlatformEnum platformEnum : values) {
            if (platformEnum.getCode().equalsIgnoreCase(code)) {
                return platformEnum;
            }
        }
        return OTHER;
    }

    public static PlatformEnum getByParam(String param) {
        PlatformEnum[] values = PlatformEnum.values();
        for (PlatformEnum platformEnum : values) {
            if (platformEnum.getParam().equalsIgnoreCase(param)) {
                return platformEnum;
            }
        }
        return OTHER;
    }

}

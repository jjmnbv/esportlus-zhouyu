package com.kaihei.esportingplus.payment.api.enums;

public enum AppTypeEnum {

    /***
     * H5表示h5-暴鸡电竞
     */
    H5("H5", "h5-暴鸡电竞"),
    /***
     * IOS_BJDJ表示AppStore应用-暴鸡电竞
     */
    IOS_BJDJ("IOS_BJDJ", "AppStore应用-暴鸡电竞"),

    /***
     * IOS_BJ表示AppStore应用-暴鸡
     */
    IOS_BJ("IOS_BJ", "AppStore应用-暴鸡"),

    /**
     * ANDROID_BJDJ表示Android应用-暴鸡电竞
     */
    ANDROID_BJDJ("ANDROID_BJDJ", "Android应用-暴鸡电竞"),

    /***
     * WECHAT_PA_BJDJ表示微信公众号应用-暴鸡电竞
     */

    WECHAT_PA_BJDJ("WECHAT_PA_BJDJ", "微信公众号应用-暴鸡电竞"),

    /***
     * WECHAT_MP_BJDJ表示微信小程序应用-暴鸡电竞
     */
    WECHAT_MP_BJDJ("WECHAT_MP_BJDJ", "微信小程序应用-暴鸡电竞");

    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    AppTypeEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * find enum by appId .
     */
    public static AppTypeEnum lookup(String appId) {
        for (AppTypeEnum appTypeEnum : values()) {
            if (appTypeEnum.value.equals(appId)) {
                return appTypeEnum;
            }
        }
        throw new IllegalArgumentException("No matching app constant for [" + appId + "]");
    }
}

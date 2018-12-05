package com.kaihei.esportingplus.payment.api.enums;

/**
 * @program: esportingplus-payment
 * @description: 操作系统(PC表示PC端 、 ANDROID表示android 、 IOS表示ios)
 * @author: xusisi
 * @create: 2018-08-18 11:29
 **/
public enum SourceType {

    /***
     * PC表示PC端
     */
    PC("PC", "PC端"),
    /**
     * ANDROID表示android
     */
    ANDROID("ANDROID", "android"),
    /***
     * IOS表示ios
     */
    IOS("IOS", "ios"),
    /***
     * 小程序
     */
    MP("MP", "小程序"),
    /***
     * H5页面
     */
    H5("H5", "H5"),
    /***
     * 微信公众号
     */
    PUBLIC_ACCOUNT("PA", "微信公众号"),
    /**
     * 暴鸡平台系统
     */
    PLATFORM("PLATFORM", "平台系统");


    private String code;

    private String msg;

    SourceType(String code, String msg) {
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

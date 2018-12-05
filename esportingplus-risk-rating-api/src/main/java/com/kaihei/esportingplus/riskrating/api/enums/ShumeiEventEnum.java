package com.kaihei.esportingplus.riskrating.api.enums;

/**
 * 调用数美api事件枚举类
 * @author chenzhenjun
 */
public enum ShumeiEventEnum {

    REGISTER("register","注册"),

    LOGIN("login","登陆"),

    ORDER("order","下单");


    /**
     * 事件编码
     */
    private String eventCode;
    /**
     * 事件说明
     */
    private String msg;

    ShumeiEventEnum(String eventCode, String msg) {
        this.eventCode = eventCode;
        this.msg = msg;
    }

    public String getEventCode() {
        return eventCode;
    }

    public String getMsg() {
        return msg;
    }

}

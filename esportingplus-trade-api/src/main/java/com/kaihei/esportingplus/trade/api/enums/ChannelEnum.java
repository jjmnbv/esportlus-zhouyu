package com.kaihei.esportingplus.trade.api.enums;

/**
 * @author admin
 */
public enum ChannelEnum {
    /** APP */
    APP (1, "APP"),
    /** 小程序 */
    MINI_PROGRAME(2, "小程序"),
    /** 未知渠道 */
    UNKNOW(1000, "未知渠道");

    //代码
    private int code;
    //描述
    private String desc;

    ChannelEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

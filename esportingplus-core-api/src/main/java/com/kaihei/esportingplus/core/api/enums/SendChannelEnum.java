package com.kaihei.esportingplus.core.api.enums;

/**
 * @Author xusisi
 * @Description 发送渠道枚举
 * @Date 2018/12/1 10:35
 **/
public enum SendChannelEnum {
    /***
     * 1 : 站内消息
     */
    INSIDE_MESSAGE(1, "站内消息"),

    /***
     * 2 : 站外push
     */
    EXTERNAL_PUSH(2, "站外push");

    private int code;

    private String msg;

    SendChannelEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

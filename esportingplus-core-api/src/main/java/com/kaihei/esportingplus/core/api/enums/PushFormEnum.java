package com.kaihei.esportingplus.core.api.enums;

/**
 * @Author xusisi
 * @Description 后续动作枚举
 * @Date 2018/12/1 10:35
 **/
public enum PushFormEnum {
    /***
     * RichContentMessage : 图文推送
     */
    RICH_CONTENT_MESSAGE(1, "图文推送", "richContentMessage"),

    /***
     * TextMessage : 纯文字推送
     */
    TEXT_MESSAGE(2, "纯文字推送", "textMessage");

    private int code;

    private String msg;

    private String type;

    PushFormEnum(int code, String msg, String type) {
        this.code = code;
        this.msg = msg;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

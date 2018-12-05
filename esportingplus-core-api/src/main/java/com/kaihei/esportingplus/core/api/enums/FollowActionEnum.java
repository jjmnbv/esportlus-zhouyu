package com.kaihei.esportingplus.core.api.enums;

/**
 * @Author xusisi
 * @Description 后续动作枚举
 * @Date 2018/12/1 10:35
 **/
public enum FollowActionEnum {

    /***
     * 1:打开APP首页
     */
    APP_INDEX(1, "打开APP首页"),

    /**
     * 2:打开APP内部页面
     */
    APP_INNER(2, "打开APP内部页面"),

    /***
     * 3:打开内部H5页面
     */
    INNER_H5(3, "打开内部H5页面"),

    /***
     * 4:打开第三方网页
     */
    THIRD_PARTY(4, "打开第三方网页");

    private int code;
    private String msg;

    FollowActionEnum(int code, String msg) {
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

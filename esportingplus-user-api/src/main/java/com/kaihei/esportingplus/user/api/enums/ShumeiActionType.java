package com.kaihei.esportingplus.user.api.enums;

/**
 * 数美操作action枚举类
 * @author chenzhenjun
 */
public enum ShumeiActionType {

    /**
     * 注册
     */
    ACTION_REGISTER(1, "注册"),

    /**
     * 登陆
     */
    ACTION_LOGIN(2, "登陆"),

    /**
     * 免下单
     */
    ACTION_ORDER(3, "下单"),

    /**
     * 接单
     */
    ACTION_ACCEPT_ORDER(4, "接单"),

    /**
     * 刷新接单列表
     */
    ACTION_REFRESH(4, "刷新接单列表");

    private Integer code;
    private String msg;

    ShumeiActionType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}

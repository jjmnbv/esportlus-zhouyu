package com.kaihei.esportingplus.user.api.enums;

/**
 * @author zhaozhenlin
 * @description: 用户兑换开关类型
 * @date: 2018/10/18 21:04
 */
public enum MessageType {
    /**
     * 旧版本订单
     */
    OLD_ORDER(1, "旧版本订单"),

    /**
     * 跳转免费车队首页（分享拉新）
     */
    FREE_TEAM(2, "分享拉新"),

    /**
     * 免费车队五星好评
     */
    STAR(3, "五星好评"),

    /**
     * 其他
     */
    OTHER(4, "其他");

    private Integer code;
    private String msg;

    MessageType(Integer code, String msg) {
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

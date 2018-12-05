package com.kaihei.esportingplus.user.api.enums;

/**
 * 用户鸡分明细类型枚举
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/10 15:44
 */
public enum UserPointItemType {

    /**
     * 兑换暴鸡值
     */
    EXCHANGE(0, "兑换暴鸡值"),

    /**
     * 完成免费车队
     */
    TEAM_DRIVE(1, "完成免费车队"),

    /**
     * 获得免费车队订单好评
     */
    TEAM_OBTAINSTAR(2, "获得免费车队订单好评");

    private Integer code;
    private String msg;

    UserPointItemType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

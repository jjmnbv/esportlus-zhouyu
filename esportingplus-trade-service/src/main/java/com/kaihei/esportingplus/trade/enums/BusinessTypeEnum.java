package com.kaihei.esportingplus.trade.enums;

/**
 * 订单业务类型枚举
 订单业务类型，0: 车队订单, 1: 滴滴订单, 2: 悬赏（约玩）订单, 3:自定义技能订单（陪玩）, 4: 专属订单
 */
public enum BusinessTypeEnum {
    /** 车队订单 */
    TEAM_ORDER(0, "车队订单"),

    /** 滴滴订单 */
    DIDI_ORDER(1, "滴滴订单"),

    /** 悬赏（约玩）订单 */
    YUEWAN_ORDER(2, "悬赏（约玩）订单"),

    /** 自定义技能订单（陪玩） */
    PEIWAN_ORDER(3, "自定义技能订单（陪玩）"),

    /** 专属订单*/
    ZHUANSHU_ORDER(4, "专属订单");

    private int code;
    private String msg;

    BusinessTypeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

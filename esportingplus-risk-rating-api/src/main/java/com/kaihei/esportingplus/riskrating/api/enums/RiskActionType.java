package com.kaihei.esportingplus.riskrating.api.enums;

/**
 * @program: esportingplus-risk-rating
 * @description: 动作类型
 * @author: chenzhenjun
 * @create: 2018-09-19 19:35
 **/
public enum RiskActionType {

    REGISTER(1,"注册"),
    LOGIN(2,"登陆"),
    ORDER(3,"下单"),
    ACCEPT_ORDER(4,"接单"),
    REFRESH(5,"刷新接单列表");
    private int code;
    private String name;

    RiskActionType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}

package com.kaihei.esportingplus.trade.api.enums;

public enum OrderTeamStatus {
    NOT_START(0, "未开车"),
    ALREADY_STATUS(1, "已开车");
    private int code;
    private String desc;

    OrderTeamStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

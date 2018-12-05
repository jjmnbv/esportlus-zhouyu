package com.kaihei.esportingplus.gamingteam.api.enums;

public enum ImMemberLeaveType {
    active(1,"主动"),UNACTIVE(2,"被动");
    private int code;
    private String desc;

    ImMemberLeaveType(int code, String desc) {
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

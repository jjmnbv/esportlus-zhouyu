package com.kaihei.esportingplus.api.enums;

/**
 * 投诉单类型
 * @author admin
 */
public enum ComplainOrderTypeEnum {
    UNKNOWN(0, "未知"),
    COMMON_(1, "普通订单投诉"),
    XUANSHANG(2, "悬赏订单投诉"),
    JINENG(3, "技能订单投诉"),
    TEAM(4, "车队订单投诉"),
    DNF(5, "DNF车队订单投诉");


    //代码
    private int code;
    //描述
    private String desc;

    ComplainOrderTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return (byte)code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}

package com.kaihei.esportingplus.trade.api.enums;

/**
 * @author admin
 */
public enum ServiceTypeEnum {
    /** 暴鸡 */
    BAOJI (1, "暴鸡"),
    /** 暴娘 */
    BAONIANG(2, "暴娘"),
    /** 车队 */
    TEAM(3, "车队"),
    /** DNF*/
    DNF(4, "DNF");

    //代码
    private int code;
    //描述
    private String desc;

    ServiceTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
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

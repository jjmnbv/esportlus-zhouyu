package com.kaihei.esportingplus.api.enums;

/**
 * 投诉类型
 * @author admin
 */
public enum ComplainTypeEnum {
    DEFAULT(0, "默认值"),
    PERFORMANCE_POOR(1, "技术不好"),
    ATTITUDE_POOR(2, "态度不好"),
    SERVICE_POOR(3, "服务不好"),
    OTHER_REASON(4, "其他原因");


    //代码
    private int code;
    //描述
    private String desc;

    ComplainTypeEnum(int code, String desc) {
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

package com.kaihei.esportingplus.api.enums;

/**
 * 投诉状态
 * @author admin
 */
public enum ComplaintStatusEnum {
    UNKNOWN(0, "全部"),
    APPEALING(1, "投诉中"),
    NEED_TWICE_CHECK(3, "需二次审核"),
    TIMEOUT_NOT_APPEAL(4, "超时未申诉"),
    APPEALED(5, "暴鸡已申诉"),
    COMPLAIN_SUCCESS(10, "投诉成立"),
    COMPLAIN_FAILURE(11, "投诉不成立");


    //代码
    private int code;
    //描述
    private String desc;

    ComplaintStatusEnum(int code, String desc) {
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

    public static String getDescByCode(Integer code) {
        for (ComplaintStatusEnum complaintStatusEnum : ComplaintStatusEnum.values()) {
            if (complaintStatusEnum.getCode() == code) {
                return complaintStatusEnum.getDesc();
            }
        }
        return null;
    }

}

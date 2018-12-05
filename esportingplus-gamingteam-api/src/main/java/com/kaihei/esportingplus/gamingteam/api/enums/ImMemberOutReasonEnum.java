package com.kaihei.esportingplus.gamingteam.api.enums;

/**
 * 踢出队员原因枚举
 *
 * @author zhangfang
 */
public enum ImMemberOutReasonEnum {

    /** 被队长踢出 */
    LEADER_OUT(1, "老铁，你已经被踢出车队了！"),
    /** 超时未支付被系统踢出 */
    PAID_OUT_TIME(2, "老铁，由于您超时未支付已经被踢出车队了");

    private int code;
    private String desc;

    ImMemberOutReasonEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ImMemberOutReasonEnum getByCode(int code) {
        ImMemberOutReasonEnum imMemberOutReasonEnum = null;
        for (ImMemberOutReasonEnum reasonEnum : ImMemberOutReasonEnum.values()) {
            if (reasonEnum.getCode() == code) {
                imMemberOutReasonEnum = reasonEnum;
                break;
            }
        }
        return imMemberOutReasonEnum;
    }
}

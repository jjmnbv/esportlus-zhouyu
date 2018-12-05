package com.kaihei.esportingplus.gamingteam.api.enums;

/**
 * 微信通知原因枚举
 *
 * @author zhangfang
 */
public enum WxPushReasonEnum {

    /** 队长解散车队 */
    LEADER_DISMISS(1, "队长解散车队"),
    /** 队员自己退出车队 */
    BOSS_QUIT(2, "你已退出车队"),
    /** 被队长踢出车队 */
    BOSS_KICKOUT(3, "你已被踢出车队");

    private int code;
    private String desc;

    WxPushReasonEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static WxPushReasonEnum getByCode(int code) {
        WxPushReasonEnum wxPushReasonEnum = null;
        for (WxPushReasonEnum reasonEnum : WxPushReasonEnum.values()) {
            if (reasonEnum.getCode() == code) {
                wxPushReasonEnum = reasonEnum;
                break;
            }
        }
        return wxPushReasonEnum;
    }
}

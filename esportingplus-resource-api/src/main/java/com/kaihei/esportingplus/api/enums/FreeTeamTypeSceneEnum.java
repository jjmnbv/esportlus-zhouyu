package com.kaihei.esportingplus.api.enums;

/**
 * 免费车队类型应用场景
 * @author liangyi
 */
public enum FreeTeamTypeSceneEnum {

    /**
     * 未知, 不存在这种场景
     */
    UNKNOWN(-1, "未知场景"),

    /**
     * 组建车队
     */
    CREATE_TEAM(1, "组建车队"),

    /**
     * 上车
     */
    JOIN_TEAM(2, "加入车队");

    private int code;
    private String msg;

    FreeTeamTypeSceneEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static FreeTeamTypeSceneEnum fromCode(Integer code) {
        FreeTeamTypeSceneEnum freeTeamTypeSceneEnum = UNKNOWN;
        FreeTeamTypeSceneEnum[] values = FreeTeamTypeSceneEnum.values();
        for (FreeTeamTypeSceneEnum codeEnum : values) {
            if (codeEnum.getCode() == code) {
                freeTeamTypeSceneEnum = codeEnum;
                break;
            }
        }
        return freeTeamTypeSceneEnum;
    }

}
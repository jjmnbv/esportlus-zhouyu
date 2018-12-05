package com.kaihei.esportingplus.api.enums;

public enum MachineTypeEnum {
    DEFAULT(0, "默认配置"), IOS_X_XS(1, "ios的x/xs版"), IOS_XSM_XR(2, "ios的xsm/xr版"), ANDROID_1080P(3,
            "android的1080p版"),
    ANDROID_18_9_RATE(4, "android的18:9全面屏版");

    private Integer code;
    private String msg;

    MachineTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static MachineTypeEnum fromCode(Integer machineType) {
        for (MachineTypeEnum machineTypeEnum : values()) {
            if (machineTypeEnum.getCode().equals(machineType)) {
                return machineTypeEnum;
            }
        }
        return null;
    }
}

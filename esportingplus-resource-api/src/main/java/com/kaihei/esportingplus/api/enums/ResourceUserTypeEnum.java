package com.kaihei.esportingplus.api.enums;

/**
 * 资源服务面向的人群枚举
 *
 * @author zhangfang
 */
public enum ResourceUserTypeEnum {
    ALL(0, "全量用户"), BOSS(1, "老板"), BAOJI_AND_BAONIANG(2, "暴鸡暴娘");
    private Integer code;
    private String msg;

    ResourceUserTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static ResourceUserTypeEnum getByCode(Integer code) {
        for (ResourceUserTypeEnum userTypeEnum : values()) {
            if (userTypeEnum.getCode().equals(code)) {
                return userTypeEnum;
            }
        }
        return null;
    }
}

package com.kaihei.esportingplus.common.enums;

/**
 * 车队分类枚举
 *
 * @author liangyi
 */
public enum TeamCategoryEnum {

    /** 免费--改为了匹配 2018/11/27 */
    FREE(0, "匹配车队"),

    /** 付费 */
    PAYING(1, "付费车队"),

    UNKNOWN(-1, "未知类型, 不存在这种情况");

    private int code;
    private String desc;

    TeamCategoryEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static TeamCategoryEnum getByCode(int code) {
        TeamCategoryEnum categoryEnum = UNKNOWN;
        TeamCategoryEnum[] values = TeamCategoryEnum.values();
        for (TeamCategoryEnum teamCategoryEnum : values) {
            if (teamCategoryEnum.getCode() == code) {
                categoryEnum =  teamCategoryEnum;
                break;
            }
        }
        return categoryEnum;
    }
}

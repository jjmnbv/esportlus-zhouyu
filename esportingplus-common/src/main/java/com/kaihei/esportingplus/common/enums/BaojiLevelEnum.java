package com.kaihei.esportingplus.common.enums;

/**
 * 暴鸡等级枚举
 *
 * @author zhangfang
 */
public enum BaojiLevelEnum {

    /** 老板 */
    BOSS(0, "老板",""),
    /** 普通暴鸡 */
    COMMON(100, "普通暴鸡","暴鸡"),
    /** 优选暴鸡 */
    PREFERENCE(101, "优选暴鸡","优选"),
    /** 超级暴鸡 */
    SUPER(102, "超级暴鸡","超级"),
    /** 暴娘 */
    BN(300, "暴娘","暴娘");

    private int code;
    private String desc;
    private String frontZh;
    BaojiLevelEnum(int code, String desc,String frontZh) {
        this.code = code;
        this.desc = desc;
        this.frontZh = frontZh;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getFrontZh() {
        return frontZh;
    }

    public static BaojiLevelEnum getByCode(int code) {
        BaojiLevelEnum[] values = BaojiLevelEnum.values();
        for (BaojiLevelEnum baojiLevelEnum : values) {
            if (baojiLevelEnum.getCode() == code) {
                return baojiLevelEnum;
            }
        }
        return null;
    }
}

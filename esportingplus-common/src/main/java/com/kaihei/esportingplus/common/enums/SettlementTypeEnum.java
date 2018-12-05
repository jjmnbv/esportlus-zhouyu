package com.kaihei.esportingplus.common.enums;

/**
 * 结算类型枚举
 *
 * @author liangyi
 */
public enum SettlementTypeEnum {

    /** 局 */
    ROUND(1, "局"),

    /** 小时 */
    HOUR(2, "小时"),

    /** 未知类型 */
    UNKNOWN(-1, "未知结算类型");

    private int code;
    private String desc;

    SettlementTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static SettlementTypeEnum getByCode(int code) {
        SettlementTypeEnum settlementTypeEnum = UNKNOWN;
        SettlementTypeEnum[] values = SettlementTypeEnum.values();
        for (SettlementTypeEnum typeEnum : values) {
            if (typeEnum.getCode() == code) {
                settlementTypeEnum = typeEnum;
                break;
            }
        }
        return settlementTypeEnum;
    }
}

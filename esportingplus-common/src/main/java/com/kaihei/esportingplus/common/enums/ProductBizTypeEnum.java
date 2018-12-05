package com.kaihei.esportingplus.common.enums;

/**
 * 产品业务类型枚举
 *
 * @author liangyi
 */
public enum ProductBizTypeEnum {

    /** 滴滴 */
    GAMING_DIDI(1, "滴滴"),

    /** 车队 */
    GAMING_TEAM(2, "车队");

    private int code;
    private String desc;

    ProductBizTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ProductBizTypeEnum getByCode(int code) {
        ProductBizTypeEnum[] values = ProductBizTypeEnum.values();
        for (ProductBizTypeEnum productBizTypeEnum : values) {
            if (productBizTypeEnum.getCode() == code) {
                return productBizTypeEnum;
            }
        }
        return null;
    }
}

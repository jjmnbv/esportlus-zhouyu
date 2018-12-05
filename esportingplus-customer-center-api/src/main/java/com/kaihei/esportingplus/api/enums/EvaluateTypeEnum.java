package com.kaihei.esportingplus.api.enums;

/**
 * 评价类型枚举
 *
 * @author yangshidong
 * @date 2018/11/15
 */
public enum EvaluateTypeEnum {
    ORDER_EVALUATE(0, "普通单评价"),
    PREMADE_EVALUATE(1, "车队单评价"),
    SERVICE_EVALUATE(2, "自定义技能单评价");
    private int code;
    private String desc;

    EvaluateTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static EvaluateTypeEnum fromCode(int code) {
        for (EvaluateTypeEnum c : EvaluateTypeEnum.values()) {
            if (c.code == code) {
                return c;
            }
        }
        return ORDER_EVALUATE;
    }

    public int getCode() {
        return code;
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
}

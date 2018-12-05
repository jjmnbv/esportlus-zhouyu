package com.kaihei.esportingplus.user.api.enums;

/**
 * @author zl.zhao
 * @description:兑换暴击值权限配置
 * @date: 2018/10/23 15:18
 */
public enum AuthorityType {
    /**
     * 可兑换暴击值
     */
    EXCHANGE_TRUE(0, "可兑换"),

    /**
     * 不可兑换暴击值
     */
    EXCHANGE_FALSE(1, "不可兑换");

    private Integer code;
    private String msg;

    AuthorityType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

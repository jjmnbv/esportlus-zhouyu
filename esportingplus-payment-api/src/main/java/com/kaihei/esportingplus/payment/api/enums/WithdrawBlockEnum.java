package com.kaihei.esportingplus.payment.api.enums;

/**
 * 提现截停枚举类
 *
 * @author chenzhenjun
 */
public enum WithdrawBlockEnum {

    NOT_YET("NOT_YET", "未截停"),

    BLOCKING("BLOCKING", "截停中"),

    TWICE("TWICE", "二次截停");

    private final String value;
    private final String name;

    WithdrawBlockEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    /**
     * find enum by channel tag.
     */
    public static WithdrawBlockEnum lookup(String value) {
        for (WithdrawBlockEnum channel : values()) {
            if (channel.value.equals(value)) {
                return channel;
            }
        }
        throw new IllegalArgumentException("No matching channel constant for [" + value + "]");
    }


}

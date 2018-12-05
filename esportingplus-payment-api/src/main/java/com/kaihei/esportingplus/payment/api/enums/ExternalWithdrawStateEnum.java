package com.kaihei.esportingplus.payment.api.enums;

/**
 * 提现订单-状态枚举类
 * @author chenzhenjun
 */
public enum ExternalWithdrawStateEnum {

    PROCESSING("PROCESSING", "处理中"),
    SUCCESS("SUCCESS", "已完成"),
    FAILED("FAILED", "失败"),
    CANCEL("CANCEL", "已取消"),
    REFUND("REFUND", "已退回");

    private final String value;
    private final String name;

    ExternalWithdrawStateEnum(String value, String name) {
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
    public static ExternalWithdrawStateEnum lookup(String value) {
        for (ExternalWithdrawStateEnum channel : values()) {
            if (channel.value == value) {
                return channel;
            }
        }
        throw new IllegalArgumentException("No matching channel constant for [" + value + "]");
    }

}

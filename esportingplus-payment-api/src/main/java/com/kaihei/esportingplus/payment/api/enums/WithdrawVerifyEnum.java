package com.kaihei.esportingplus.payment.api.enums;

/**
 * 提现审批枚举类
 *
 * @author chenzhenjun
 */
public enum WithdrawVerifyEnum {

    WAIT("TRANSFER_VERIFY_WAIT", "待审批(未提现)"),

    VERIFY_SUCCESS("TRANSFER_VERIFY_SUCCESS", "审批通过(提现处理中)"),

    SUCCESS("TRANSFER_SUCCESS", "审批通过(提现成功)"),

    VERIFY_FAIL("TRANSFER_VERIFY_FAIL", "审批拒绝(提现失败)"),

    FAIL("TRANSFER_FAIL", "审批通过(提现失败)");

    private final String value;
    private final String name;

    WithdrawVerifyEnum(String value, String name) {
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
    public static WithdrawVerifyEnum lookup(String value) {
        for (WithdrawVerifyEnum channel : values()) {
            if (channel.value.equals(value)) {
                return channel;
            }
        }
        throw new IllegalArgumentException("No matching channel constant for [" + value + "]");
    }

}

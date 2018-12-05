package com.kaihei.esportingplus.payment.api.enums;

/**
 * 兼容之前python提现接口，前端不用修改
 *
 * @author chenzhenjun
 */
public enum WithdrawChannelEnum {

    ALI(2, "C003", "云账户提现-支付宝"),
    WECHAT(3, "C004", "云账户提现-微信");

    private final Integer code;
    private final String value;
    private final String name;

    WithdrawChannelEnum(Integer code, String value, String name) {
        this.code = code;
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
    public static WithdrawChannelEnum lookup(Integer code) {
        for (WithdrawChannelEnum channel : values()) {
            if (channel.code.intValue() == code) {
                return channel;
            }
        }
        throw new IllegalArgumentException("请选择正确的提现渠道！");
    }

    public static WithdrawChannelEnum lookupByValue(String value) {
        for (WithdrawChannelEnum channel : values()) {
            if (channel.value.equals(value)) {
                return channel;
            }
        }
        throw new IllegalArgumentException("请选择正确的提现渠道！");
    }

    public static WithdrawChannelEnum lookupByName(String name) {
        for (WithdrawChannelEnum channel : values()) {
            if (channel.name.equals(name)) {
                return channel;
            }
        }
        throw new IllegalArgumentException("请选择正确的提现渠道！");
    }

}

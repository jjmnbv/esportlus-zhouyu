package com.kaihei.esportingplus.payment.api.enums;

/**
 * 云账户提现渠道枚举类
 * 方便python传参及入库
 * @author chenzhenjun
 */
public enum CloudChannelEnum {

    BANKCARD("bank", "银行卡"),
    WECHAT("C004", "微信"),
    ALI("C003", "支付宝");

    private final String value;
    private final String name;

    CloudChannelEnum(String value, String name) {
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
    public static CloudChannelEnum lookup(String value) {
        for (CloudChannelEnum channel : values()) {
            if (channel.value.equals(value)) {
                return channel;
            }
        }
        throw new IllegalArgumentException("No matching channel constant for [" + value + "]");
    }
}

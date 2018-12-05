package com.kaihei.esportingplus.payment.api.enums;

/**
 * 支付渠道
 *
 * @author haycco
 **/
public enum PayChannelEnum {

    /**
     * 暴鸡币钱包
     */
    WALLET_PAY(1, "C001", "暴鸡币钱包支付", "暴鸡币"),
    /**
     * QQ-APP支付
     */
    QQ_APP_PAY(2, "C002", "QQAPP支付", "QQ"),
    /**
     * 微信-APP支付
     */
    WECHAT_APP_PAY(3, "C003", "微信APP支付", "微信"),
    /**
     * 支付宝-APP支付
     */
    ALI_APP_PAY(4, "C004", "支付宝APP支付", "支付宝"),
    /**
     * IOS内购
     */
    APPLE_PAY(5, "C005", "苹果支付", "苹果"),
    /**
     * 平台系统
     */
    PLATFORM_SYSTEM(6, "C006", "平台系统支付", "平台系统"),
    /**
     * 云账户
     */
    CLOUD_ACCOUNT_PAY(7, "C007", "云账户支付", "云账户"),
    /**
     * 微信公众号支付
     */
    WECHAT_PA_PAY(8, "C008", "微信公众号支付", "微信"),
    /**
     * 微信小程序支付
     */
    WECHAT_MP_PAY(9, "C009", "微信小程序支付", "微信"),

    /***
     * qq小程序支付
     */
    QQ_MP_PAY(10, "C010", "QQ小程序支付", "QQ"),

    /***
     * qq公众号支付
     */
    QQ_PA_PAY(11, "C011", "QQ公众号支付", "QQ"),

    /***
     * 微信内打开的H5支付
     */
    WECHAT_INNER_H5_PAY(12, "C012", "微信内打开的微信H5支付", "微信"),
    /***
     * 微信外打开的H5支付
     */
    WECHAT_OUTER_H5_PAY(13, "C013", "其他浏览器打开的微信H5支付", "微信"),

    /**
     * 支付宝H5支付
     */
    ALI_H5_PAY(14, "C014", "支付宝H5支付", "支付宝");

    private final String value;
    private final String name;
    private final int code;
    private final String channelName;

    PayChannelEnum(int code, String value, String name, String channelName) {
        this.code = code;
        this.value = value;
        this.name = name;
        this.channelName = channelName;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getChannelName() {
        return channelName;
    }

    /**
     * Return a string of this channel tag.
     */
    @Override
    public String toString() {
        return this.value;
    }

    /**
     * find enum by channel tag.
     */
    public static PayChannelEnum lookup(String channelTag) {
        for (PayChannelEnum channel : values()) {
            if (channel.value.equals(channelTag)) {
                return channel;
            }
        }
        throw new IllegalArgumentException("No matching channel constant for channelTag [" + channelTag + "]");
    }

    public static PayChannelEnum lookup(int code) {
        for (PayChannelEnum channel : values()) {
            if (channel.code == code) {
                return channel;
            }
        }
        throw new IllegalArgumentException("No matching channel constant for code [" + code + "]");
    }

}

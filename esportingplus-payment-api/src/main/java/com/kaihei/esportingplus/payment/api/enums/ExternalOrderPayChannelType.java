package com.kaihei.esportingplus.payment.api.enums;

/**
 * @program: esportingplus
 * @description: 订单支付类型
 * @author: xusisi
 * @create: 2018-11-05 10:12
 **/
public enum ExternalOrderPayChannelType {
    /***
     * ALIPAY支付宝
     */
    ALIPAY("ALIPAY", "支付宝"),

    /***
     * 微信/QQ支付
     */
    TENPAY("TENPAY", "微信/QQ支付"),

    /***
     * 云账户
     */
    CLOUDACCOUNT("CLOUDACCOUNT", "云账户");

    private String value;

    private String name;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    ExternalOrderPayChannelType(String value, String name) {
        this.value = value;
        this.name = name;
    }

}

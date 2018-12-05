package com.kaihei.esportingplus.payment.api.enums;

public enum CloudReturnCodeEnum {

    SUCCESS("0000", " 成功"),
    PARAMS_MISSING("1000", "缺少请求参数"),
    SIGN_EXPIRED("1001", "签名已过期"),
    ERROR_PATTERN("1002", "请求参数格式不正确"),
    SIGN_ERROR("1003", "签名错误"),
    ENCRYPT_ERROR("1004", "加密错误"),
    APPKEY_MISSING("1005", "商户未设置3deskey或没有设置appkey"),
    UPLOAD_ERROR("2001", "上传数据有误"),
    UPLOADED("2002", "已上传过该笔流⽔"),
    AUTH_FAIL("2003", "实名认证失败"),
    CARDNO_ERROR("2006", "银⾏卡号错误"),
    ORDER_MISSING_1("2011", "订单不存在"),
    ORDER_MISSING_8("2018", "订单不存在"),
    ERROR_AMOUNT("2016", "错误的打款金额"),
    AMOUNT_BELOW_ZERO("2024", "订单⾦额⼩于0"),
    BALANCE_LACK("2027", "账户余额不⾜"),
    DEALER_BELONG_ERROR("2027", "该商户不属于该经纪公司"),
    LINK_FAIL("3000", "银⾏系统连接失败，请联系我们修复"),
    BILL_NOT_EXIST("5000", "不存在的账单");

    private final String value;
    private final String name;

    CloudReturnCodeEnum(String value, String name) {
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
    public static CloudReturnCodeEnum lookup(String value) {
        for (CloudReturnCodeEnum channel : values()) {
            if (channel.value.equals(value)) {
                return channel;
            }
        }
        throw new IllegalArgumentException("No matching channel constant for [" + value + "]");
    }
}

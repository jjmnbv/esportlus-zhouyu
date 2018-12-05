package com.kaihei.esportingplus.trade.api.enums;

/**
 * 微信支付状态枚举
 *
 * @author Orochi-Yzh
 * @dateTime 2018/1/24 18:19
 * @updatetor
 */
public enum WxPayStatusEnum {
    SUCCESS("SUCCESS", "支付成功"),
    REFUND("REFUND", "转入退款"),
    NOTPAY("NOTPAY", "未支付"),
    CLOSED("CLOSED", "已关闭"),
    REVOKED("REVOKED", "已撤销（刷卡支付）"),
    USERPAYING("USERPAYING", "用户支付中"),
    PAYERROR("PAYERROR", "支付失败(其他原因，如银行返回失败)"),
    UNKNOWN("UNKNOWN", "未知的微信支付状态");

    //代码
    private String code;
    //描述
    private String desc;

    WxPayStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static WxPayStatusEnum fromCode(String code) {
        for (WxPayStatusEnum c : WxPayStatusEnum.values()) {
            if (c.code.equals(code)) {
                return c;
            }
        }
        return PAYERROR;
    }

    public static WxPayStatusEnum fromDesc(String desc) {
        for (WxPayStatusEnum c : WxPayStatusEnum.values()) {
            if (c.desc.equals(desc)) {
                return c;
            }
        }
        return UNKNOWN;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static void main(String[] args) {
        System.out.println();
    }
}
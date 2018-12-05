package com.kaihei.esportingplus.payment.api.enums;

/**
 * @program: esportingplus-payment
 * @description: 暴鸡币订单表类型枚举
 * @author: xusisi
 * @create: 2018-08-17 11:29
 **/
public enum GCoinOrderEnum {
	//交易状态
    /***
     * 001订单创建
     */
	TRANSACTION_STATE_CREATE("001", "创建订单"),
    /***
     * 002已支付
     */
	TRANSACTION_STATE_PAYSUCCESS("002", "已支付"),
    /***
     * 003退款
     */
	TRANSACTION_STATE_REFUND("003", "退款"),

	//终端类型
    /***
     * PC
     */
	SOURCE_TYPE_PC("PC", "电脑端"),
    /***
     * ANDROID
     */
	SOURCE_TYPE_ANDROID("ANDROID", "安卓端"),
    /***
     * IOS
     */
	SOURCE_TYPE_IOS("IOS", "苹果端");

    private String code;

    private String msg;

    GCoinOrderEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

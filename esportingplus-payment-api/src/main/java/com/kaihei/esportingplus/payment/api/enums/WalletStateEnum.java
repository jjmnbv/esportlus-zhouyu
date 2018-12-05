package com.kaihei.esportingplus.payment.api.enums;

/**
 * @description: 用户钱包状态枚举
 * @author: xiaolijun
 * @create: 2018-08-17 11:29
 **/
public enum WalletStateEnum {
    /***
     * available表示可用
     * 
     */
	WALLET_STATE_AVAILABLE("AVAILABLE", "可用"),
    /***
     * frozen表示冻结
     */
	WALLET_STATE_FROZEN("FROZEN", "冻结"),
    /***
     * unavailable表示不可用
     */
	WALLET_STATE_UNAVAILABLE("UNAVAILABLE", "不可用");

    private String code;

    private String msg;

    WalletStateEnum(String code, String msg) {
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

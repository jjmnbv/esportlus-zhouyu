package com.kaihei.esportingplus.payment.mq.message;

/**
 * @program: esportingplus
 * @description: 内部支付-暴鸡币支付消息
 * @author: xusisi
 * @create: 2018-11-17 16:25
 **/
public class WalletPayNotifyMQ {

    private String msg;

    private String code;

    private String orderId;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}

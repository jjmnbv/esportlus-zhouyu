package com.kaihei.esportingplus.payment.api.enums;

/**
 * @program: esportingplus
 * @description: 暴鸡币充值类型
 * @author: xusisi
 * @create: 2018-10-18 17:11
 **/
public enum GCoinRechargeTypeEnum {


    /***
     * 000 前台用户主动充值
     */
    USER_RECHARGE("000", "前台用户主动充值"),

    /***
     * 001	投诉补偿
     */
    COMPLAINT_COMPENSATION("001", "投诉补偿"),

    /***
     * 002	订单补贴
     */
    ORDER_SUBSIDY("002", "订单补贴"),

    /***
     * 003	活动补贴
     */
    ACTIVITY_SUBSIDY("003", "活动补贴"),

    /***
     * 004	消费奖励
     */
    CONSUMPTION_REWARD("004", "消费奖励");

    private String code;

    private String msg;

    GCoinRechargeTypeEnum(String code, String msg) {
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

package com.kaihei.esportingplus.trade.api.enums;

/**
 * RPG车队队员动作触发的订单操作枚举
 *
 * @author Orochi-Yzh
 * @dateTime 2018/1/24 18:19
 * @updatetor
 */
public enum TeamOrderRPGActionEnum {

    PREPARE_JOIN_TEAM_KICK(0, "待入团，被队长踢出车队","触发的订单动作:暴鸡=无订单，老板=订单【取消】并【退款】"),

    PREPARE_JOIN_TEAM_QUIT(1, "待入团，主动退出","触发的订单动作:暴鸡=无订单，老板=订单【取消】并【退款】"),

    PREPARE_JOIN_TEAM_DISMISSD(2, "待入团，队长解散车队","触发的订单动作:暴鸡=无订单，老板=订单【取消】并【退款】"),

    JOINED_TEAM_KICK(3, "已入团，被队长踢出车队","触发的订单动作:暴鸡=无订单，老板=订单【取消】并【退款】"),

    JOINED_TEAM_QUIT(4, "已入团，主动退出","触发的订单动作:暴鸡=无订单，老板=订单【完成】，【退款】"),

    JOINED_TEAM_DISMISSD(5, "已入团，队长解散车队","触发的订单动作:暴鸡=无订单，老板=订单【取消】并【退款】"),

    TEAM_STARTED_QUIT(6, "已开车，主动退出","暴鸡:订单【取消】并【无收益】，老板=订单【完成】，【不退款】"),

    TEAM_STARTED_DISMISSD(7, "已开车，队长解散车队","暴鸡:订单【完成】并根据结果结算收益，老板=订单【完成】并根据结果是否【退款】"),

    TEAM_STARTED_FINISH(8, "已开车，队长结束车队(车队已完成)","暴鸡:订单【完成】并根据结果结算收益，老板=订单【完成】并根据结果是否【退款】"),

    PRE_PROFIT(9, "实时计算暴鸡收益","返回预收益金额"),

    PAY_TIME_OUT(10, "支付超时被系统踢出+未支付被队长踢出+未支付主动退出+未支付解散车队","更新车队队员订单状态为：10"),

    PAID_REFUND(11, "支付成功后，发现已不在车队中，发起退款。","发起退款"),

    UNKNOWN(-1, "未知或错误状态，忽略处理","未知或错误状态，忽略处理");

    //代码
    private int code;
    //描述
    private String desc;
    //触发的订单操作
    private String action;

    TeamOrderRPGActionEnum(int code, String desc,String action) {
        this.code = code;
        this.desc = desc;
        this.action = action;
    }

    public static TeamOrderRPGActionEnum fromCode(int code) {
        for (TeamOrderRPGActionEnum c : TeamOrderRPGActionEnum.values()) {
            if (c.code == code) {
                return c;
            }
        }
        return UNKNOWN;
    }

    public static TeamOrderRPGActionEnum fromDesc(String desc) {
        for (TeamOrderRPGActionEnum c : TeamOrderRPGActionEnum.values()) {
            if (c.desc.equals(desc)) {
                return c;
            }
        }
        return UNKNOWN;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
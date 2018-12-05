package com.kaihei.esportingplus.trade.api.enums;

/**
 * PVP车队队员动作触发的订单操作枚举
 *
 * @author Orochi-Yzh
 * @dateTime 2018/1/24 18:19
 * @updatetor
 */
public enum TeamOrderPVPActionEnum {

    PREPARE_KICK(0, "待准备，被队长踢出车队","无订单"),

    PREPARE_QUIT(1, "待准备，主动退出","无订单"),

    PREPARE_DISMISSD(2, "待准备，队长解散车队","无订单"),

    PREPARED_KICK(3, "已准备，被队长踢出车队","无订单"),

    PREPARED_QUIT(4, "已准备，主动退出","无订单"),

    PREPARED_DISMISSD(5, "已准备，队长解散车队","无订单"),

    PAID_KICK(6, "已支付&未开车，被队长踢出车队","暴鸡:无订单，老板=订单【取消】+【退款】"),

    PAID_QUIT(7, "已支付&未开车，主动退出","暴鸡:无订单，老板=订单【取消】+【退款】"),

    PAID_DISMISSD(8, "已支付&未开车，队长解散车队","暴鸡:无订单，老板=订单【取消】+【退款】"),

    TEAM_STARTED_QUIT(9, "已开车，主动退出","暴鸡:订单【完成】并根据结果结算收益，老板=订单【完成】并根据结果是否【退款】"),

    TEAM_STARTED_DISMISSD(10, "已开车，队长解散车队","暴鸡:订单【完成】并根据结果结算收益，老板=订单【完成】并根据结果是否【退款】"),

    TEAM_STARTED_FINISH(11, "已开车，队长正常结束车队","暴鸡:订单【完成】并根据结果结算收益，老板=订单【完成】并根据结果是否【退款】"),

    PAID_REFUND_OTHER(12, "已支付&未开车，有队员退出","老板全员退款"),

    UNKNOWN(-1, "未知或错误状态，忽略处理","未知或错误状态，忽略处理");

    //代码
    private int code;
    //描述
    private String desc;
    //触发的订单操作
    private String action;

    TeamOrderPVPActionEnum(int code, String desc,String action) {
        this.code = code;
        this.desc = desc;
        this.action = action;
    }

    public static TeamOrderPVPActionEnum fromCode(int code) {
        for (TeamOrderPVPActionEnum c : TeamOrderPVPActionEnum.values()) {
            if (c.code == code) {
                return c;
            }
        }
        return UNKNOWN;
    }

    public static TeamOrderPVPActionEnum fromDesc(String desc) {
        for (TeamOrderPVPActionEnum c : TeamOrderPVPActionEnum.values()) {
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
package com.kaihei.esportingplus.trade.api.enums;

/**
 * 队员动作触发的订单操作枚举
 *
 * @author Orochi-Yzh
 * @dateTime 2018/1/24 18:19
 * @updatetor
 */
public enum OrderStatusEnum {
    READY(0, "准备中(暴鸡队员）","服务中"),
    READY_PAY(1, "待付款（老板上车后的状态）",""),
    PAYED(2, "已付款（老板付款后的状态）","服务中"),
    PAY_CANCEL(3, "已取消","服务中退出"),
    FINISH(4, "已完成","已完成"),
    UNKNOWN(-1, "未知状态","未知");

    //代码
    private int code;
    //描述
    private String desc;
    private String freeFrontZh;
    OrderStatusEnum(int code, String desc,String freeFrontZh) {
        this.code = code;
        this.desc = desc;
        this.freeFrontZh =freeFrontZh;
    }

    public static OrderStatusEnum fromCode(int code) {
        for (OrderStatusEnum c : OrderStatusEnum.values()) {
            if (c.code == code) {
                return c;
            }
        }
        return UNKNOWN;
    }

    public static OrderStatusEnum fromDesc(String desc) {
        for (OrderStatusEnum c : OrderStatusEnum.values()) {
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

    public String getFreeFrontZh() {
        return freeFrontZh;
    }

    public static void main(String[] args) {
        System.out.println();
    }
}
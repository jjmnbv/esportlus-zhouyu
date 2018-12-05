package com.kaihei.esportingplus.payment.api.enums;

/**
 * @description: 业务类型
 * @author: xiaolijun
 **/
public enum BusinessTypeEnum {

    /**
     * 王者荣耀
     */
    WZRY("B001", "王者荣耀"),
    /**
     * 英雄联盟
     */
    LOL("B002","英雄联盟"),
    /**
     * 绝地求生
     */
    JDQS("B003","绝地求生"),
    /**
     * 刺激战场
     */
    CJZC("B004","刺激战场"),
    /**
     * QQ飞车
     */
    QQFC("B005","QQ飞车"),
    /**
     * 全军出击
     */
    QJCJ("B006","全军出击"),
    /**
     * 荒野行动
     */
    HYXD("B007","荒野行动"),
    /**
     * 决战平安京
     */
    JZPAJ("B008","决战平安京"),
    /**
     * 堡垒之夜
     */
    BLZY("B009","堡垒之夜"),
    /**
     * 投诉补偿
     */
    COMPLAINT_COMPENSATE("B010","投诉补偿"),
    /**
     * 订单补偿
     */
    ORDER_COMPENSATE("B011","订单补偿"),
    /**
     * 活动补偿
     */
    ACTIVITY_COMPENSATE("B012","活动补偿");

    private String code;
    private String msg;

    BusinessTypeEnum(String code, String msg) {
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

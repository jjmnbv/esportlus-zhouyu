package com.kaihei.esportingplus.core.api.enums;

/**
 * @Author liuyang
 * @Description 消息分类
 * @Date 2018/11/13 10:35
 **/
public enum MessageTypeEnum {

    /** 订单*/
    ORDER("订单"),

    /**系统通知*/
    SYSTEM("系统通知"),

    /**官方公告*/
    OFFICIAL("官方公告"),

    /**聊天、语音*/
    CHAT("聊天、语音");

    private String desc;

    MessageTypeEnum(String desc) {
        this.desc = desc;
    }

}

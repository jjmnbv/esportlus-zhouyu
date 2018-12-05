package com.kaihei.esportingplus.core.constant;

/**
 * @Author liuyang
 * @Description 消息常量
 * @Date 2018/11/5 11:04
 **/
public interface MessageConstants {

    interface Type {
        //单聊
        int PRIVATE = 1;
        //群聊
        int GROUP = 2;
        //系统
        int SYSTEM = 3;

        int PUSH = 4;

        /***
         * 融云推送
         */
        int RON_YUN_PUSH = 5;

    }

    interface Constants {
        String IM_USER_ADMIN = "bjdj_admin";
    }

}

package com.kaihei.esportingplus.core.constant;

/**
 * @Author liuyang
 * @Description 融云常量
 * @Date 2018/10/26 16:36
 **/
public interface RongYunConstants {

    interface Result {
        int SUCCESSCODE = 200;
        int OVERRATELIMIT = 1008; // 超出了调用频率限制

    }

    interface Limit {
        int MAX_BLOCK_MINUTE = 43200;  // 最大封禁时间， 单位：分
        int MAX_USERS_PER_PRIVATE = 1000;  // 每次单点推送最大用户数
        int MAX_USERS_PER_SYSTEM = 100; // 每次系统会话推送最大用户数
        int MAX_GROUPS_PER_SYSTEM = 3; // 每次系统会话推送最大群组数
        int MAX_USERS_PER_TAG = 1000;  // 每次设置标签的最大用户数，非文档标注，仅作分割用
        int MAX_MINITE_PER_PRIVITE = 6000;  // 每分钟最多推送的最大用户数
        int MAX_SECOND_PER_GROUP = 20;  // 每秒钟最多推送的最大群组数
    }

    interface BuiltIn {
        /**文本消息类型*/
        String MSG_TYPE_TXT = "RC:TxtMsg";

        /**图片消息类型*/
        String MSG_TYPE_IMG = "RC:ImgMsg";

        /** 语音消息类型*/
        String MSG_TYPE_VOICE = "RC:VcMsg";

        /**图文消息类型*/
        String MSG_TYPE_IMGTXT = "RC:ImgTextMsg";

        /** 位置消息类型*/
        String MSG_TYPE_LBS = "RC:LBSMsg";

        /** 添加联系人消息类型*/
        String MSG_TYPE_CONTACT = "RC:ContactNtf";

        /** 提示条（小灰条）消息提示类型*/
        String MSG_TYPE_INFO = "RC:InfoNtf";

        /** 资料通知消息类型*/
        String MSG_TYPE_PROFILE = "RC:ProfileNtf";

        /** 群组通知*/
        String MSG_TYPE_GROUP = "RC:GrpNtf";

        /** 讨论组通知类型*/
        String MSG_TYPE_DIZ = "RC:DizNtf";

        /** 通用命令通知消息*/
        String MSG_TYPE_CMDNTF = "RC:CmdNtf";

        /** 命令消息*/
        String MSG_TYPE_CMD = "RC:CmdMsg";
    }


}

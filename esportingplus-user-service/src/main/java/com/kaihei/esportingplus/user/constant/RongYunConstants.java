package com.kaihei.esportingplus.user.constant;

/**
 * @author yangshidong
 * @Title: RongYunConstants
 * @Description: 融云服务常量
 * @date 2018/9/14 15:10
 */
public class RongYunConstants {
    //融云系统内置消息类型
    public static final String MSG_TYPE_TXT = "RC:TxtMsg";  // 文本消息类型
    public static final String MSG_TYPE_IMG = "RC:ImgMsg";  // 图片消息类型
    public static final String MSG_TYPE_VOICE = "RC:VcMsg";  // 语音消息类型
    public static final String MSG_TYPE_IMGTXT = "RC:ImgTextMsg";  // 图文消息类型
    public static final String MSG_TYPE_LBS = "RC:LBSMsg";  // 位置消息类型
    public static final String MSG_TYPE_CONTACT = "RC:ContactNtf";  // 添加联系人消息类型
    public static final String MSG_TYPE_INFO = "RC:InfoNtf";  // 提示条（小灰条）消息提示类型
    public static final String MSG_TYPE_PROFILE = "RC:ProfileNtf";  // 资料通知消息类型
    public static final String MSG_TYPE_GROUP = "RC:GrpNtf";  // 群组通知
    public static final String MSG_TYPE_DIZ = "RC:DizNtf";  // 讨论组通知类型
    public static final String MSG_TYPE_CMDNTF = "RC:CmdNtf";  // 通用命令通知消息
    public static final String MSG_TYPE_CMD = "RC:CmdMsg";  // 命令消息

    //自定义系统消息类型
    public static final String MSG_TYPE_CUSTOM = "BAOJI:NOTIF";
    public static final String MSG_TYPE_FEED = "FEED:NOTIFY";
    public static final String MSG_TYPE_CARD = "BAOJI:CARD";  // 暴鸡名片推送
    public static final String MSG_TYPE_IMGTEXT_MSG = "BAOJI:ImgTextMsg" ; // 自定义图文消息
    public static final String MSG_TYPE_CAR_INVITE = "BAOJI:INVITE";
    public static final String MSG_TYPE_TITLE_CONTENT = "BAOJI:TITLE_CONTENT"; // 标题+文本内容 类型
    public static final String MSG_TYPE_TEXT_MSG = "BAOJI:TextMsg";     // 新版自定义通知，支持跳转
    public static final String MSG_TYPE_SERVICE_ORDER_JUMP_MSG = "BAOJI:ServiceOrderJump";  // 自定义服务订单消息-支持跳转
    public static final String MSG_TYPE_SERVICE_ORDER_JUMP_ACTOIN_MSG = "BAOJI:ServiceOrderJumpAction";  // 自定义服务订单消息-支持跳转-支持挂起操作
    public static final String MSG_TYPE_SERVICE_ORDER_TEXT_MSG = "BAOJI:ServiceOrderTextMsg";  // 自定义服务订单消息-支持跳转
    public static final String MSG_TYPE_SERVICE_CARD_TEXT_MSG = "BAOJI:ServiceCardTextMsg";  // 自定义服务卡片消息-支持跳转


    public static final int MAX_BLOCK_MINUTE = 43200;  // 最大封禁时间， 单位：分
    public static final int MAX_USERS_PER_PRIVATE = 1000;  // 每次单点推送最大用户数
    public static final int MAX_USERS_PER_SYSTEM = 64 ; // 每次系统会话推送最大用户数
    public static final int MAX_GROUPS_PER_SYSTEM = 3 ; // 每次系统会话推送最大群组数
    public static final int MAX_USERS_PER_TAG = 1000;  // 每次设置标签的最大用户数，非文档标注，仅作分割用
    public static final int MAX_MINITE_PER_PRIVITE = 6000;  // 每分钟最多推送的最大用户数
    public static final int MAX_SECOND_PER_GROUP = 20;  // 每秒钟最多推送的最大群组数
}

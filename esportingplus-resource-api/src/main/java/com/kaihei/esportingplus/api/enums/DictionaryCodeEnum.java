package com.kaihei.esportingplus.api.enums;

/**
 * 数据字典 code 枚举类
 * 根据需要从 dictionary 表的 code字段自行添加
 * @author liangyi
 */
public enum DictionaryCodeEnum {

    /**
     * 免费车队配置
     */
    FREE_TEAM_CONFIG_CODE("free_team_config_mfcdpz", "免费车队配置code"),
    TEAM_GENERAL_CONFIG_CODE("team_general_config_cdcgpz", "车队常规配置code"),
    PLAY_MODE_POINTS("0", "游戏模式_上分code"),
    PLAY_MODE_PLAY("1", "游戏模式_陪玩code"),
    SETTLEMENT_ROUND("1", "结算类型_局code"),
    SETTLEMENT_HOUR("2", "结算类型_小时code"),

    USER_REGISTER_REWARD("user_register_reward", "新用户注册奖励配置"),
    USER_START_UP_REWARD("user_start_up_reward", "用户启动奖励配置"),
    INVITE_FRIENDS_REWARD("invite_friends_reward", "邀请好友奖励配置"),
    FRIENDS_CONSUME_REWARD("friends_amount_consume_reward", "好友消费奖励配置"),
    FRIENDS_FINISH_TEAM_REWARD("friends_finish_team_reward", "好友完成车队奖励配置"),
    FRIEND_INVITE_FRIENDS("friends_invite_friends_reward", "好友成功邀请新人配置");

    private String code;
    private String msg;

    DictionaryCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static DictionaryCodeEnum fromCode(String code) {
        DictionaryCodeEnum dictionaryCodeEnum = null;
        DictionaryCodeEnum[] values = DictionaryCodeEnum.values();
        for (DictionaryCodeEnum codeEnum : values) {
            if (codeEnum.getCode().equals(code)) {
                dictionaryCodeEnum = codeEnum;
                break;
            }
        }
        return dictionaryCodeEnum;
    }

}
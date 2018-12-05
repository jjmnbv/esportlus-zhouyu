package com.kaihei.esportingplus.api.enums;

/**
 * 数据字典分类 code 枚举类
 * @author liangyi
 */
public enum DictionaryCategoryCodeEnum {

    /**
     * 暴鸡身份分类
     */
    BAOJI_IDENTITY("baoji_identity", "暴鸡身份类"),
    BAOJI_LEVEL("baoji_level", "暴鸡等级类"),
    BAOJI_LEVEL_RATE("baoji_level_rate", "暴鸡等级系数"),
    FREE_TEAM_CONFIG("free_team_config", "免费车队配置"),
    FREE_TEAM_TYPE("free_team_type", "免费车队类型"),
    TEAM_GENERAL_CONFIG("team_general_config", "车队常规配置"),
    GAME("game", "游戏类"),
    GAME_DAN("game_dan", "游戏段位类"),
    SHARE_INVITE("share_invite","分享邀请配置"),
    THIRD_PARTY_SHARE("third_party_share","第三方分享文案配置"),
    CHICKEN_CONFIG("chicken_config", "小鸡配置"),
    APP_VERSION_CONFIG("app_version_config", "客户端版本信息配置"),
    BJDJ_RONGYUN_ADMIN("bjdj_rongyun_admin", "官方融云用户信息"),
    OFFICIAL_ACCOUNT_CONFIG("official_account_config", "官方账号信息配置"),
    BANNER_CAROUSEL_CONFIG("banner_carousel_config","banner轮播配置"),
    SETTLEMENT_TYPE("settlement_type", "结算类型");

    private String code;
    private String msg;

    DictionaryCategoryCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static DictionaryCategoryCodeEnum fromCode(String code) {
        DictionaryCategoryCodeEnum categoryCodeEnum = null;
        DictionaryCategoryCodeEnum[] values = DictionaryCategoryCodeEnum.values();
        for (DictionaryCategoryCodeEnum codeEnum : values) {
            if (codeEnum.getCode().equals(code)) {
                categoryCodeEnum = codeEnum;
                break;
            }
        }
        return categoryCodeEnum;
    }

}
package com.kaihei.esportingplus.api.enums;

public enum FreeTeamHomeCategoryCodeEnum {
    FREE_TEAM_HOME_ADVERTISE_IMAGE("free_team_home_advertise_image","免费车队首页宣传图"),
    FREE_TEAM_HOME_SCROLL_WORDS_TEMPLATE("free_team_home_scroll_words_template","免费车队首页滚动文字模板");
    private String code;
    private String msg;

    FreeTeamHomeCategoryCodeEnum(String code, String msg) {
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

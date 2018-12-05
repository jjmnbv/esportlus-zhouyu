package com.kaihei.esportingplus.api.enums;

public enum BannerCodeEnum {
    BANNER_CAROUSE_CONFIG("banner_carouse_config","banner轮播数量时间配置");

    private String code;
    private String msg;

    BannerCodeEnum(String code, String msg) {
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

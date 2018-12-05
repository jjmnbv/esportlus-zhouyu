package com.kaihei.esportingplus.marketing.api.enums;

/**
 * 分享的类型枚举
 *
 * @author linruihe
 * @version 1.0
 * @date 2018/12/3 15:44
 */
public enum ShareEnum {

    /**
     * 分享拉新
     */
    SHARE_FREE("share_copy_free_team", "分享拉新");

    private String code;
    private String msg;

    ShareEnum(String code, String msg) {
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

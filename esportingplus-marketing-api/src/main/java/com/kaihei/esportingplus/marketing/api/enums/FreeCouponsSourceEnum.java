package com.kaihei.esportingplus.marketing.api.enums;

/**
 * @author zl.zhao
 * @description:免费次数券来源
 * @date: 2018/12/3 17:21
 */
public enum FreeCouponsSourceEnum {
    /**
     * 每日启动赠送
     */
    SOURCE_SYSTEM_GIVE(1, "每日启动赠送"),


    /**
     * 邀请任务
     */
    SOURCE_INVITE_GIVE(2, "邀请任务");

    private Integer code;
    private String msg;

    FreeCouponsSourceEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

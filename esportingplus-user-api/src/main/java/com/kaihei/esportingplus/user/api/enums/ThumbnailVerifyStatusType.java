package com.kaihei.esportingplus.user.api.enums;

public enum ThumbnailVerifyStatusType {

    MACHINE_VERIFY(0,"待机器审核"),
    VERIFY(1, "待人工审核"),
    VERIFY_FAIL(2, "审核不通过"),
    NORMAL(3, "正常");

    private Integer code;
    private String msg;

    ThumbnailVerifyStatusType(Integer code, String msg) {
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

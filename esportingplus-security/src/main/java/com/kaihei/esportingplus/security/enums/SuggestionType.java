package com.kaihei.esportingplus.security.enums;

/**
阿里云建议用户执行的操作
 */
public enum SuggestionType {
    /** 文本正常*/
    PASS("pass", "文本正常"),
    /**需要人工审核*/
    REVIEW("review","需要人工审核"),
    /**文本违规*/
    BLOCK("block","文本违规");

    private String code;
    private String msg;

    SuggestionType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code.toString();
    }

    public String getMsg() {
        return msg;
    }
}

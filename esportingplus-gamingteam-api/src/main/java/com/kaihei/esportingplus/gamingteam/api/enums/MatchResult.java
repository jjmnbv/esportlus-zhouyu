package com.kaihei.esportingplus.gamingteam.api.enums;

public enum  MatchResult {
    SUCCESS(0,"成功"),FAIL(1,"失败");
    private Integer code;
    private String desc;

    MatchResult(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
    public static MatchResult getByCode(Integer code){
        for(MatchResult result :values()){
            if(result.getCode().equals(code)){
                return result;
            }
        }
        return MatchResult.FAIL;
    }
}

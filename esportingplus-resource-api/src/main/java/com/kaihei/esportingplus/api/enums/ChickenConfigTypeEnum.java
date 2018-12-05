package com.kaihei.esportingplus.api.enums;

public enum ChickenConfigTypeEnum {
    /**
     * 小鸡配置
     */
    IOS_PERSON_CENTER(1, "ios个人中心小鸡配置"),IOS_FREE_TEAM(2, "ios免费车队小鸡配置"),Android_PERSON_CENTER(3, "Android个人中心小鸡配置"),Android_FREE_TEAM(4, "Android免费车队小鸡配置");

    private Integer code;
    private String msg;

    ChickenConfigTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static ChickenConfigTypeEnum fromCode(Integer code){
        if(code==null) {
            return null;
        }
        for(ChickenConfigTypeEnum chickenConfigTypeEnum :values()){
            if(chickenConfigTypeEnum.getCode().intValue()==code){
                return chickenConfigTypeEnum;
            }
        }
        return null;
    }
}

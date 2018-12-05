package com.kaihei.esportingplus.api.enums;

public enum UserTypeEnum {
    BOSS(1,"老板"),BAOJI(2,"暴鸡");

    private Integer code;
    private String msg;

    UserTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static UserTypeEnum fromCode(Integer userType){
        for(UserTypeEnum userTypeEnum :values()){
            if(userTypeEnum.getCode().equals(userType)){
                return userTypeEnum;
            }
        }
        return null;
    }
}

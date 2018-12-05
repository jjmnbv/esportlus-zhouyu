package com.kaihei.esportingplus.api.enums;

import com.kaihei.esportingplus.common.tools.ObjectTools;

public enum BannerPositionEnum {
    FREE_TEAM_HOME("banner_free_team_home", "免费车队首页"), BAOJI_BN_CENTER("banner_baoji_bn_center", "暴鸡中心"),MY_FOOT("my_foot","我的底部");
    private String code;
    private String desc;

    BannerPositionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static BannerPositionEnum getByCode(String code){
        if(ObjectTools.isEmpty(code)){
            return  null;
        }
        for(BannerPositionEnum positionEnum :values()){
            if(positionEnum.getCode().equals(code)){
                return positionEnum;
            }
        }
        return null;
    }
}


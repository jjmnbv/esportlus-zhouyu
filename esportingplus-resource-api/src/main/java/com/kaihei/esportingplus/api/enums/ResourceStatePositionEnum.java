package com.kaihei.esportingplus.api.enums;

import com.kaihei.esportingplus.common.tools.ObjectTools;

/**
 * 资源位位置枚举
 * @author zhangfang
 */
public enum ResourceStatePositionEnum {
    FREE_TEAM_HOME("resource_free_team_middle", "免费车队首页中");
    private String code;
    private String desc;

    ResourceStatePositionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ResourceStatePositionEnum getByCode(String code){
        if(ObjectTools.isEmpty(code)){
            return  null;
        }
        for(ResourceStatePositionEnum positionEnum :values()){
            if(positionEnum.getCode().equals(code)){
                return positionEnum;
            }
        }
        return null;
    }
}


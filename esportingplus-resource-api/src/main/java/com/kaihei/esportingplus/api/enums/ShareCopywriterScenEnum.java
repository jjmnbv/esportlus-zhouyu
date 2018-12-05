package com.kaihei.esportingplus.api.enums;

import com.kaihei.esportingplus.common.tools.ObjectTools;

public enum ShareCopywriterScenEnum {
    SHARE_COPY_FREE_TEAM("share_copy_free_team","免费车队分享文案场景");

    private String code;
    private String msg;

    ShareCopywriterScenEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static ShareCopywriterScenEnum getByCode(String code){
        if(ObjectTools.isEmpty(code)){
            return  null;
        }
        for(ShareCopywriterScenEnum scenEnum :values()){
            if(scenEnum.getCode().equals(code)){
                return scenEnum;
            }
        }
        return null;
    }
}

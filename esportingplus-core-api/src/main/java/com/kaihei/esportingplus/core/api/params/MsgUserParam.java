package com.kaihei.esportingplus.core.api.params;

import org.hibernate.validator.constraints.URL;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
public class MsgUserParam {

    /**用户 Id*/
    @NotBlank(message = "用户id不能为空")
    private String userId;

    /**用户名称*/
    @NotBlank(message = "用户名称不能为空")
    private String name;

    /**用户头像 URI*/
//    @URL(message = "用户头像地址不合法")
    private String portraitUri;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortraitUri() {
        return portraitUri;
    }

    public void setPortraitUri(String portraitUri) {
        this.portraitUri = portraitUri;
    }
}

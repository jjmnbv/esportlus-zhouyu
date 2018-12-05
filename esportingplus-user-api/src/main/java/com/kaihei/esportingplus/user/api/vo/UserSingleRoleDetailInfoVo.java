package com.kaihei.esportingplus.user.api.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class UserSingleRoleDetailInfoVo extends UserGameBaseRoleInfoVo {
    private static final long serialVersionUID = -5687943891278199221L;

    private UserGameUserCredentialVo credential;

    public UserGameUserCredentialVo getCredential() {
        return credential;
    }

    public void setCredential(UserGameUserCredentialVo credential) {
        this.credential = credential;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

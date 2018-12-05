package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;
import java.util.List;

public class UserGameDetailRoleInfoVo extends UserGameBaseRoleInfoVo implements Serializable{

    private static final long serialVersionUID = 4440172872863731655L;

    private List<UserGameUserCredentialVo> credentials;

    public List<UserGameUserCredentialVo> getCredentials() {
        return credentials;
    }

    public void setCredentials(
            List<UserGameUserCredentialVo> credentials) {
        this.credentials = credentials;
    }
}

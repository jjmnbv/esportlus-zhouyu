package com.kaihei.esportingplus.user.api.params;

import java.io.Serializable;

/**
 * @Auther: chen.junyong
 * @Date: 2018-10-24 14:17
 * @Description:
 */
public class MiniprogramLoginParam implements Serializable {

    private static final long serialVersionUID = -5625485950336115828L;

    private String code;
    private MiniprogramUserInfo userInfo;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setUserInfo(MiniprogramUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public MiniprogramUserInfo getUserInfo() {
        return userInfo;
    }
}

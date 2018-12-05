package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;

/**
 * @Auther: chen.junyong
 * @Date: 2018-10-25 16:04
 * @Description:
 */
public class RegistLoginUpdateRtokenMessageVo extends ConsumeBasicVo implements Serializable {

    private static final long serialVersionUID = 1991033771459113037L;

    private String uid;
    private String rToken;

    public String getrToken() {
        return rToken;
    }

    public void setrToken(String rToken) {
        this.rToken = rToken;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

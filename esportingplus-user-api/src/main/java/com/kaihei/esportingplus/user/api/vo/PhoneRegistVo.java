package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;

/**
 * @Auther: chen.junyong
 * @Date: 2018-09-20 14:59
 * @Description: 手机注册相应类
 */
public class PhoneRegistVo implements Serializable {

    private static final long serialVersionUID = 2938499262980135763L;

    private String token;

    private String rcloud_token;

    private String said;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRcloud_token() {
        return rcloud_token;
    }

    public void setRcloud_token(String rcloud_token) {
        this.rcloud_token = rcloud_token;
    }

    public String getSaid() {
        return said;
    }

    public void setSaid(String said) {
        this.said = said;
    }
}

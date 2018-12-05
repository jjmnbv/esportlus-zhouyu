package com.kaihei.esportingplus.user.api.params;

import java.io.Serializable;

/**
 * @Auther: chen.junyong
 * @Date: 2018-10-24 14:23
 * @Description:
 */
public class MiniprogramUserInfo implements Serializable {

    private static final long serialVersionUID = 7582337201154396561L;

    private String encryptedData;
    private String iv;

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }
}

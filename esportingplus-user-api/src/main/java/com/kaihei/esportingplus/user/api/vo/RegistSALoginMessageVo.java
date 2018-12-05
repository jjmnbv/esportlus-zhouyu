package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: chen.junyong
 * @Date: 2018-09-21 15:11
 * @Description:
 */
public class RegistSALoginMessageVo extends ConsumeBasicVo implements Serializable {

    private static final long serialVersionUID = -7154227835951637695L;

    private String version;

    private Date lastLogin;

    private String channel;

    private String uid;

    private String platform;

    public RegistSALoginMessageVo() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}

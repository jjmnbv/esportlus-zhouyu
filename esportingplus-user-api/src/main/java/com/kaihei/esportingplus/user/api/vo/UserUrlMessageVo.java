package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: linruihe
 * @Date: 2018-09-21 15:11
 * @Description:
 */
public class UserUrlMessageVo extends ConsumeBasicVo implements Serializable {

    private static final long serialVersionUID = -7154227835951637695L;

    private String version;

    private Date lastLogin;

    private String channel;

    private int id;

    private int userId;

    private String url;

    public UserUrlMessageVo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;

/**
 * 用户信息
 */
public class UserInfoVo implements Serializable {
    private static final long serialVersionUID = 1741002779638708705L;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 用户ID
     */
    private String uid;

    /**
     * 用户头像
     */
    private String avatar;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "UserInfoVo{" +
                "username='" + username + '\'' +
                ", uid='" + uid + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}

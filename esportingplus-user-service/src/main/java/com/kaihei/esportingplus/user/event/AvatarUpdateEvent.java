package com.kaihei.esportingplus.user.event;

import com.kaihei.esportingplus.common.event.Event;

/**
 * 修改用户头像事件
 * @author
 */
public class AvatarUpdateEvent implements Event {

    /**
     * 用户uid
     */
    private String uid;
    /**
     * 要修改的用户头像
     */
    private String avatar;

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
}

package com.kaihei.esportingplus.marketing.api.event;

import java.io.Serializable;

/**
 * @Auther: chen.junyong
 * @Date: 2018-10-09 10:06
 * @Description:
 */
public class UserRegistEvent extends UserEvent implements Serializable {

    private static final long serialVersionUID = -7219826407535216363L;
    public static final int STATUS_ON = 1, STATUS_OFF = 0;

    /**
     * 邀请人
     */
    private String uid;

    /**
     * 被邀请人
     */
    private String invitedUid;

    private Integer invitedUserId;
    private Integer userId;

    private int invitCount;

    /**
     * 数美ID
     */
    private String deviceId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public int getInvitCount() {
        return invitCount;
    }

    public void setInvitCount(int invitCount) {
        this.invitCount = invitCount;
    }

    public UserRegistEvent() {}

    public Integer getInvitedUserId() {
        return invitedUserId;
    }

    public void setInvitedUserId(Integer invitedUserId) {
        this.invitedUserId = invitedUserId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getInvitedUid() {
        return invitedUid;
    }

    public void setInvitedUid(String invitedUid) {
        this.invitedUid = invitedUid;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}

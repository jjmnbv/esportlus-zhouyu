package com.kaihei.esportingplus.auth.server.event;

import com.kaihei.esportingplus.common.event.Event;

/**
 * 缓存用户
 */

public class StoreUserEvent implements Event {

    private String pyToken;
    private String token;
    private String uid;

    public StoreUserEvent(String pyToken,String token,String uid){
        this.pyToken = pyToken;
        this.token = token;
        this.uid = uid;
    }


    public String getPyToken() {
        return pyToken;
    }

    public void setPyToken(String pyToken) {
        this.pyToken = pyToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

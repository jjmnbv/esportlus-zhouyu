package com.kaihei.esportingplus.marketing.api.event;

import java.io.Serializable;

/**
 * @author zl.zhao
 * @description:
 * @date: 2018/10/25 18:13
 */
public class UserExemptionEvent  extends UserEvent implements Serializable {
    private static final long serialVersionUID = 7564996325398780180L;

    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
